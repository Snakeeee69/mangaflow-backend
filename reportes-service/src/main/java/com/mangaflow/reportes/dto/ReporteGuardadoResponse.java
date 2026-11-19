package com.mangaflow.reportes.dto;

import com.mangaflow.reportes.model.TipoReporte;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReporteGuardadoResponse {

    private Long id;
    private TipoReporte tipoReporte;

    private Integer totalPedidos;
    private Integer pedidosPendientes;
    private Integer pedidosCompletados;

    private Integer totalPagos;
    private Integer pagosAprobados;
    private Integer pagosRechazados;

    private Double totalVendido;

    private LocalDateTime fechaGeneracion;
    private Boolean activo;
}
