package com.mangaflow.notificaciones.service;

import com.mangaflow.notificaciones.client.PersonasClient;
import com.mangaflow.notificaciones.dto.CrearNotificacionRequest;
import com.mangaflow.notificaciones.dto.NotificacionResponse;
import com.mangaflow.notificaciones.model.Notificacion;
import com.mangaflow.notificaciones.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PersonasClient personasClient;

    public NotificacionService(NotificacionRepository notificacionRepository, PersonasClient personasClient) {
        this.notificacionRepository = notificacionRepository;
        this.personasClient = personasClient;
    }

    public List<NotificacionResponse> listarNotificaciones() {
        return notificacionRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificacionResponse buscarPorId(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe notificación con ID: " + id));

        if (!notificacion.getActivo()) {
            throw new RuntimeException("La notificación está desactivada");
        }

        return toResponse(notificacion);
    }

    public List<NotificacionResponse> buscarPorPersona(Long personaId) {
        return notificacionRepository.findByPersonaIdAndActivoTrue(personaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificacionResponse> buscarNoLeidasPorPersona(Long personaId) {
        return notificacionRepository.findByPersonaIdAndLeidaFalseAndActivoTrue(personaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificacionResponse crearNotificacion(CrearNotificacionRequest request) {

        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se puede crear notificación: la persona no existe");
        }

        Notificacion notificacion = Notificacion.builder()
                .personaId(request.getPersonaId())
                .titulo(request.getTitulo())
                .mensaje(request.getMensaje())
                .tipoNotificacion(request.getTipoNotificacion())
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .fechaLectura(null)
                .activo(true)
                .build();

        notificacionRepository.save(notificacion);

        return toResponse(notificacion);
    }

    public NotificacionResponse marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe notificación con ID: " + id));

        if (!notificacion.getActivo()) {
            throw new RuntimeException("La notificación está desactivada");
        }

        if (Boolean.TRUE.equals(notificacion.getLeida())) {
            throw new IllegalArgumentException("La notificación ya estaba marcada como leída");
        }

        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());

        notificacionRepository.save(notificacion);

        return toResponse(notificacion);
    }

    public boolean existeNotificacion(Long id) {
        return notificacionRepository.existsByIdAndActivoTrue(id);
    }

    public void eliminarNotificacion(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe notificación con ID: " + id));

        notificacion.setActivo(false);
        notificacionRepository.save(notificacion);
    }

    private NotificacionResponse toResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
                .id(notificacion.getId())
                .personaId(notificacion.getPersonaId())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .tipoNotificacion(notificacion.getTipoNotificacion())
                .leida(notificacion.getLeida())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaLectura(notificacion.getFechaLectura())
                .activo(notificacion.getActivo())
                .build();
    }
}
