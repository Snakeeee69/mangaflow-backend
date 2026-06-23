package com.mangaflow.personas.dto;

import com.mangaflow.personas.model.Rol;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonaResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private Rol rol;
    private Boolean activo;
}
