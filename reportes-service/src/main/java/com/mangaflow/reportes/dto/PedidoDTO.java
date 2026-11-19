package com.mangaflow.reportes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

    private Long id;
    private Long personaId;
    private Long mangaId;
    private Integer cantidad;
    private String estado;
    private Double total;
    private Boolean activo;
}
