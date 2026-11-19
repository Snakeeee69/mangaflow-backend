package com.mangaflow.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mangaflow.auth.client.PersonasClient;
import com.mangaflow.auth.dto.LoginRequest;
import com.mangaflow.auth.dto.LoginResponse;
import com.mangaflow.auth.model.Credencial;
import com.mangaflow.auth.repository.CredencialRepository;
import com.mangaflow.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceApplicationTests {

    @Mock
    private CredencialRepository credencialRepository;

    @Mock
    private PersonasClient personasClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void testLoginConMockito() {
        LoginRequest request = new LoginRequest();
        request.setCorreo("usuarioPrueba@mail.com");
        request.setPassword("12345");

        Credencial credencialSimulada = Credencial.builder()
                .id(1L)
                .personaId(10L)
                .correo("usuarioPrueba@mail.com")
                .passwordEncriptada("password_encriptada_en_bd")
                .activo(true)
                .build();

        when(credencialRepository.findByCorreoAndActivoTrue(anyString()))
            .thenReturn(Optional.of(credencialSimulada));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        LoginResponse response = authService.login(request);

        assertNotNull(response, "La respuesta no debería ser nula");
    }
}