package com.mangaflow.auth.dto;

import com.mangaflow.auth.model.RolUsuario;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {

    private Long personaId;
    private String correo;
    private RolUsuario rol;
    private String token;
    private String mensaje;
}
