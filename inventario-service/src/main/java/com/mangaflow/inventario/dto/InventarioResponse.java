package com.mangaflow.inventario.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventarioResponse {

    private Long id;
    private Long mangaId;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;
}
