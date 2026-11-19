package com.mangaflow.puntos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CuentaPuntosResponse {

    private Long id;
    private Long personaId;
    private Integer saldo;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}
