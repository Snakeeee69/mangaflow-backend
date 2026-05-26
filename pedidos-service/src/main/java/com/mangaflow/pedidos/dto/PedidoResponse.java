package com.mangaflow.pedidos.dto;

import com.mangaflow.pedidos.model.EstadoPedido;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PedidoResponse {

    private Long id;
    private Long personaId;
    private Long mangaId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}
