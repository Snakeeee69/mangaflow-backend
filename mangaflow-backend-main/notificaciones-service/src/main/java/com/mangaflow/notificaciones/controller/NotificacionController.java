package com.mangaflow.notificaciones.controller;

import com.mangaflow.notificaciones.dto.CrearNotificacionRequest;
import com.mangaflow.notificaciones.dto.NotificacionResponse;
import com.mangaflow.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listarNotificaciones() {
        return ResponseEntity.ok(notificacionService.listarNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<NotificacionResponse>> buscarPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(notificacionService.buscarPorPersona(personaId));
    }

    @GetMapping("/persona/{personaId}/no-leidas")
    public ResponseEntity<List<NotificacionResponse>> buscarNoLeidasPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(notificacionService.buscarNoLeidasPorPersona(personaId));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeNotificacion(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.existeNotificacion(id));
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> crearNotificacion(
            @Valid @RequestBody CrearNotificacionRequest request
    ) {
        NotificacionResponse response = notificacionService.crearNotificacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponse> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.ok("Notificación desactivada correctamente");
    }
}