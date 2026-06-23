package com.mangaflow.catalogo.dto;

import com.mangaflow.catalogo.model.FormatoManga;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MangaResponse {

    private Long id;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private String demografia;
    private String sinopsis;
    private Integer numeroTomo;
    private Double precioFisico;
    private Double precioDigital;
    private FormatoManga formato;
    private Boolean activo;
}
