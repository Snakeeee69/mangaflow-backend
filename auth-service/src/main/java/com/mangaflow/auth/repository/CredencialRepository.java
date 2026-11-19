package com.mangaflow.auth.repository;

import com.mangaflow.auth.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByCorreoAndActivoTrue(String correo);

    Optional<Credencial> findByPersonaIdAndActivoTrue(Long personaId);

    List<Credencial> findByActivoTrue();

    boolean existsByCorreo(String correo);

    boolean existsByIdAndActivoTrue(Long id);
}
