package com.mangaflow.auth.dto;

import com.mangaflow.auth.model.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroAuthRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 60, message = "La contraseña debe tener entre 4 y 60 caracteres")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}