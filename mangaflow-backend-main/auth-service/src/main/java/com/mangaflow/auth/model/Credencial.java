package com.mangaflow.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "credenciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long personaId;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String passwordEncriptada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    private LocalDateTime ultimoLogin;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
