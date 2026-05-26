package com.mangaflow.personas.repository;

import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {



    Optional<Persona> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Persona> findByRol(Rol rol);

    List<Persona> findByActivoTrue();
}
