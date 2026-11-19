package com.mangaflow.puntos.repository;

import com.mangaflow.puntos.model.MovimientoPuntos;
import com.mangaflow.puntos.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoPuntosRepository extends JpaRepository<MovimientoPuntos, Long> {

    List<MovimientoPuntos> findByPersonaId(Long personaId);

    boolean existsByPagoIdAndTipoMovimiento(Long pagoId, TipoMovimiento tipoMovimiento);
}
