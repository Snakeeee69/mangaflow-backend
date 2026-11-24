package com.mangaflow.auth.service;

import com.mangaflow.auth.client.PersonasClient;
import com.mangaflow.auth.dto.AuthResponse;
import com.mangaflow.auth.dto.LoginRequest;
import com.mangaflow.auth.dto.LoginResponse;
import com.mangaflow.auth.dto.RegistroAuthRequest;
import com.mangaflow.auth.model.Credencial;
import com.mangaflow.auth.repository.CredencialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final CredencialRepository credencialRepository;
    private final PersonasClient personasClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            CredencialRepository credencialRepository,
            PersonasClient personasClient,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.credencialRepository = credencialRepository;
        this.personasClient = personasClient;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public AuthResponse registrar(RegistroAuthRequest request) {
        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se puede registrar credencial: la persona no existe");
        }

        if (credencialRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("No se puede registrar credencial: el correo ya está registrado");
        }

        Credencial credencial = Credencial.builder()
                .personaId(request.getPersonaId())
                .correo(request.getCorreo())
                .passwordEncriptada(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .fechaRegistro(LocalDateTime.now())
                .ultimoLogin(null)
                .activo(true)
                .build();

        credencialRepository.save(credencial);
        return toResponse(credencial);
    }

    public LoginResponse login(LoginRequest request) {
        Credencial credencial = credencialRepository.findByCorreoAndActivoTrue(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), credencial.getPasswordEncriptada())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos");
        }

        credencial.setUltimoLogin(LocalDateTime.now());
        credencialRepository.save(credencial);

        String token = jwtService.generarToken(credencial);

        emailService.enviarAlertaLogin(
                credencial.getCorreo(),
                String.valueOf(credencial.getRol())
        );

        return LoginResponse.builder()
                .personaId(credencial.getPersonaId())
                .correo(credencial.getCorreo())
                .rol(credencial.getRol())
                .token(token)
                .mensaje("Login exitoso")
                .build();
    }

    public List<AuthResponse> listarCredenciales() {
        return credencialRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AuthResponse buscarPorId(Long id) {
        Credencial credencial = credencialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe credencial con ID: " + id));
        if (!credencial.getActivo()) {
            throw new RuntimeException("La credencial está desactivada");
        }
        return toResponse(credencial);
    }

    public AuthResponse buscarPorPersona(Long personaId) {
        Credencial credencial = credencialRepository.findByPersonaIdAndActivoTrue(personaId)
                .orElseThrow(() -> new RuntimeException("No existe credencial activa para la persona ID: " + personaId));
        return toResponse(credencial);
    }

    public boolean existeCredencial(Long id) {
        return credencialRepository.existsByIdAndActivoTrue(id);
    }

    public AuthResponse desactivarCredencial(Long id) {
        Credencial credencial = credencialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe credencial con ID: " + id));
        if (!credencial.getActivo()) {
            throw new RuntimeException("La credencial ya estaba desactivada");
        }
        credencial.setActivo(false);
        credencialRepository.save(credencial);
        return toResponse(credencial);
    }

    private AuthResponse toResponse(Credencial credencial) {
        return AuthResponse.builder()
                .id(credencial.getId())
                .personaId(credencial.getPersonaId())
                .correo(credencial.getCorreo())
                .rol(credencial.getRol())
                .fechaRegistro(credencial.getFechaRegistro())
                .ultimoLogin(credencial.getUltimoLogin())
                .activo(credencial.getActivo())
                .build();
    }
}