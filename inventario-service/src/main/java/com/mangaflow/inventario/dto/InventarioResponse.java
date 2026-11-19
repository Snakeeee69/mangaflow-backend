package com.mangaflow.inventario.dto;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {

    private Long id;
    private Long mangaId;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;
}
