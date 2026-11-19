package com.mangaflow.puntos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mangaflow.puntos.model.CuentaPuntos;

import com.mangaflow.puntos.model.CuentaPuntos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaPuntosRepository extends JpaRepository<CuentaPuntos, Long> {

    Optional<CuentaPuntos> findByPersonaId(Long personaId);

    boolean existsByPersonaId(Long personaId);

    List<CuentaPuntos> findByActivoTrue();
}
