package com.mangaflow.descuentos.dto;

import com.mangaflow.descuentos.model.TipoDescuento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DescuentoResponse {

    private Long id;
    private Long personaId;
    private Long mangaId;
    private Double precioOriginal;
    private Double porcentajeDescuento;
    private Double montoDescuento;
    private Double precioFinal;
    private TipoDescuento tipoDescuento;
    private String motivo;
    private LocalDateTime fechaCalculo;
    private Boolean activo;
}
