package com.mangaflow.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long personaId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipoNotificacion;

    @Column(nullable = false)
    private Boolean leida;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaLectura;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
