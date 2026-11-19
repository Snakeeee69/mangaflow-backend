package com.mangaflow.auth.controller;

import com.mangaflow.auth.dto.AuthResponse;
import com.mangaflow.auth.dto.LoginRequest;
import com.mangaflow.auth.dto.LoginResponse;
import com.mangaflow.auth.dto.RegistroAuthRequest;
import com.mangaflow.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mangas/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroAuthRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping
    public ResponseEntity<?> listarCredenciales() {
        return ResponseEntity.ok(authService.listarCredenciales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(authService.buscarPorId(id));
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<AuthResponse> buscarPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(authService.buscarPorPersona(personaId));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeCredencial(@PathVariable Long id) {
        return ResponseEntity.ok(authService.existeCredencial(id));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<AuthResponse> desactivarCredencial(@PathVariable Long id) {
        return ResponseEntity.ok(authService.desactivarCredencial(id));
    }
}
