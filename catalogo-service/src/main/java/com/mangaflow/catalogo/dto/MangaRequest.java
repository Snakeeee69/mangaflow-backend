package com.mangaflow.catalogo.dto;

import com.mangaflow.catalogo.model.FormatoManga;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MangaRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @NotBlank(message = "La demografía es obligatoria")
    private String demografia;

    private String sinopsis;

    @NotNull(message = "El número de tomo es obligatorio")
    private Integer numeroTomo;

    @NotNull(message = "El precio físico es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio físico debe ser mayor a 0")
    private Double precioFisico;

    @NotNull(message = "El precio digital es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio digital debe ser mayor a 0")
    private Double precioDigital;

    @NotNull(message = "El formato es obligatorio")
    private FormatoManga formato;

    private Boolean activo;
}
