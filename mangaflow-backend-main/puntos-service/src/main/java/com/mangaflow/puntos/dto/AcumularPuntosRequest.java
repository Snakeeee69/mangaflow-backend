package com.mangaflow.puntos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcumularPuntosRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "El ID del pago es obligatorio")
    private Long pagoId;
}
