package com.mangaflow.puntos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas_puntos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaPuntos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long personaId;

    @Column(nullable = false)
    private Integer saldo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
