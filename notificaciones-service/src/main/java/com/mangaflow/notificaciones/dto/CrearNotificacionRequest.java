package com.mangaflow.notificaciones.dto;

import com.mangaflow.notificaciones.model.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearNotificacionRequest {

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long personaId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres")
    private String mensaje;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipoNotificacion;
}
