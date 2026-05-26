package com.mangaflow.puntos.dto;

import com.mangaflow.puntos.model.TipoMovimiento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MovimientoPuntosResponse {

    private Long id;
    private Long personaId;
    private Long pagoId;
    private TipoMovimiento tipoMovimiento;
    private Integer puntos;
    private String descripcion;
    private LocalDateTime fechaMovimiento;
}
