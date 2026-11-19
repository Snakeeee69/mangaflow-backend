package com.mangaflow.reportes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReporteResumenResponse {

    private Integer totalPedidos;
    private Integer pedidosPendientes;
    private Integer pedidosCompletados;

    private Integer totalPagos;
    private Integer pagosAprobados;
    private Integer pagosRechazados;

    private Double totalVendido;

    private String mensaje;
}
