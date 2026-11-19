package com.mangaflow.auth.dto;

import com.mangaflow.auth.model.RolUsuario;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AuthResponse {

    private Long id;
    private Long personaId;
    private String correo;
    private RolUsuario rol;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoLogin;
    private Boolean activo;
}
