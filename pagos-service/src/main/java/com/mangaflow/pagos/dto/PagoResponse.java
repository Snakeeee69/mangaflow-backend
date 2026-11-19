package com.mangaflow.pagos.dto;

import com.mangaflow.pagos.model.EstadoPago;
import com.mangaflow.pagos.model.MetodoPago;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PagoResponse {

    private Long id;
    private Long pedidoId;
    private Double monto;
    private MetodoPago metodoPago;
    private EstadoPago estado;
    private LocalDateTime fechaPago;
    private Boolean activo;
}
