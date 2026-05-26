package com.mangaflow.inventario.repository;

import com.mangaflow.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByMangaId(Long mangaId);

    boolean existsByMangaId(Long mangaId);

    List<Inventario> findByActivoTrue();

    List<Inventario> findByStockLessThanEqual(Integer stock);
}