package com.mangaflow.descuentos.repository;

import com.mangaflow.descuentos.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescuentoRepository extends JpaRepository<Descuento, Long> {

    List<Descuento> findByPersonaId(Long personaId);

    List<Descuento> findByMangaId(Long mangaId);

    List<Descuento> findByActivoTrue();

    boolean existsByIdAndActivoTrue(Long id);
    
}
