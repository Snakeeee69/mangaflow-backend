package com.mangaflow.notificaciones.repository;

import com.mangaflow.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByActivoTrue();

    List<Notificacion> findByPersonaIdAndActivoTrue(Long personaId);

    List<Notificacion> findByPersonaIdAndLeidaFalseAndActivoTrue(Long personaId);

    boolean existsByIdAndActivoTrue(Long id);
}
