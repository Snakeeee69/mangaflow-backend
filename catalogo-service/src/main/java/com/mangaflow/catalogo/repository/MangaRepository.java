package com.mangaflow.catalogo.repository;

import com.mangaflow.catalogo.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MangaRepository extends JpaRepository<Manga, Long> {

    List<Manga> findByTituloContainingIgnoreCase(String titulo);

    List<Manga> findByAutorContainingIgnoreCase(String autor);

    List<Manga> findByGeneroIgnoreCase(String genero);

    List<Manga> findByDemografiaIgnoreCase(String demografia);

    List<Manga> findByActivoTrue();

    boolean existsByTituloIgnoreCaseAndNumeroTomo(String titulo, Integer numeroTomo);
}
