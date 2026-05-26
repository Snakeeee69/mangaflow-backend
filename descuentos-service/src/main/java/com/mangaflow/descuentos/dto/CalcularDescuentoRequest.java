package com.mangaflow.descuentos.dto;

import com.mangaflow.descuentos.model.TipoDescuento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalcularDescuentoRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "El ID del manga es obligatorio")
    private Long mangaId;

    @NotNull(message = "El precio original es obligatorio")
    @Positive(message = "El precio original debe ser mayor a 0")
    private Double precioOriginal;

    @NotNull(message = "El tipo de descuento es obligatorio")
    private TipoDescuento tipoDescuento;
}
