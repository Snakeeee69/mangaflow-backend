package com.mangaflow.catalogo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "mangas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    @Column(nullable = false)
    private String editorial;

    @NotBlank(message = "El género es obligatorio")
    @Column(nullable = false)
    private String genero;

    @NotBlank(message = "La demografía es obligatoria")
    @Column(nullable = false)
    private String demografia;

    @Column(length = 1000)
    private String sinopsis;

    @NotNull(message = "El número de tomo es obligatorio")
    @Column(nullable = false)
    private Integer numeroTomo;

    @NotNull(message = "El precio físico es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio físico debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioFisico;

    @NotNull(message = "El precio digital es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio digital debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioDigital;

    @NotNull(message = "El formato es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoManga formato;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
