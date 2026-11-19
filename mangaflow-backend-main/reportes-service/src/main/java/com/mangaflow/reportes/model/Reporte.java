package com.mangaflow.reportes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReporte tipoReporte;

    @Column(nullable = false)
    private Integer totalPedidos;

    @Column(nullable = false)
    private Integer pedidosPendientes;

    @Column(nullable = false)
    private Integer pedidosCompletados;

    @Column(nullable = false)
    private Integer totalPagos;

    @Column(nullable = false)
    private Integer pagosAprobados;

    @Column(nullable = false)
    private Integer pagosRechazados;

    @Column(nullable = false)
    private Double totalVendido;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
