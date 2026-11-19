package com.mangaflow.descuentos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "descuentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long personaId;

    @Column(nullable = false)
    private Long mangaId;

    @Column(nullable = false)
    private Double precioOriginal;

    @Column(nullable = false)
    private Double porcentajeDescuento;

    @Column(nullable = false)
    private Double montoDescuento;

    @Column(nullable = false)
    private Double precioFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDescuento tipoDescuento;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
