package com.mangaflow.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "El ID del manga es obligatorio")
    private Long mangaId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    private Double precioUnitario;
}
