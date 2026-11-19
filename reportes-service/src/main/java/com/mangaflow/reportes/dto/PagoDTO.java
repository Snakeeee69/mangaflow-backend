package com.mangaflow.reportes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoDTO {

    private Long id;
    private Long pedidoId;
    private String metodoPago;
    private Double monto;
    private String estado;
    private Boolean activo;
}
