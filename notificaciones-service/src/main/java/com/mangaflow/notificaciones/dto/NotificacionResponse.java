package com.mangaflow.notificaciones.dto;

import com.mangaflow.notificaciones.model.TipoNotificacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificacionResponse {

    private Long id;
    private Long personaId;
    private String titulo;
    private String mensaje;
    private TipoNotificacion tipoNotificacion;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;
    private Boolean activo;
}
