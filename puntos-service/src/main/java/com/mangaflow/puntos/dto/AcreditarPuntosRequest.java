package com.mangaflow.puntos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcreditarPuntosRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "Los puntos a acreditar son obligatorios")
    @Positive(message = "Los puntos deben ser mayores a 0")
    private Integer puntos;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}