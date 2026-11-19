package com.mangaflow.reportes.repository;

import com.mangaflow.reportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByActivoTrue();

    boolean existsByIdAndActivoTrue(Long id);
}