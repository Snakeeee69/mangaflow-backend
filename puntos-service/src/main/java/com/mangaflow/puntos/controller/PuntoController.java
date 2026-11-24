package com.mangaflow.puntos.controller;

import com.mangaflow.puntos.dto.AcreditarPuntosRequest;
import com.mangaflow.puntos.dto.AcumularPuntosRequest;
import com.mangaflow.puntos.dto.CanjearPuntosRequest;
import com.mangaflow.puntos.dto.CuentaPuntosResponse;
import com.mangaflow.puntos.dto.MovimientoPuntosResponse;
import com.mangaflow.puntos.service.PuntoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/puntos")
public class PuntoController {

    private final PuntoService puntoService;

    public PuntoController(PuntoService puntoService) {
        this.puntoService = puntoService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaPuntosResponse>> listarCuentas() {
        return ResponseEntity.ok(puntoService.listarCuentas());
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<CuentaPuntosResponse> obtenerCuentaPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(puntoService.obtenerCuentaPorPersona(personaId));
    }

    @GetMapping("/movimientos/persona/{personaId}")
    public ResponseEntity<List<MovimientoPuntosResponse>> listarMovimientosPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(puntoService.listarMovimientosPorPersona(personaId));
    }

    @GetMapping("/persona/{personaId}/existe")
    public ResponseEntity<Boolean> existeCuentaPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(puntoService.existeCuentaPorPersona(personaId));
    }

    @PostMapping("/acumular")
    public ResponseEntity<CuentaPuntosResponse> acumularPuntos(@Valid @RequestBody AcumularPuntosRequest request) {
        CuentaPuntosResponse response = puntoService.acumularPuntos(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/acreditar")
    public ResponseEntity<CuentaPuntosResponse> acreditarPuntos(@Valid @RequestBody AcreditarPuntosRequest request) {
        CuentaPuntosResponse response = puntoService.acreditarPuntos(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/canjear")
    public ResponseEntity<CuentaPuntosResponse> canjearPuntos(@Valid @RequestBody CanjearPuntosRequest request) {
        CuentaPuntosResponse response = puntoService.canjearPuntos(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/persona/{personaId}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable Long personaId) {
        puntoService.eliminarCuenta(personaId);
        return ResponseEntity.ok("Cuenta de puntos desactivada correctamente");
    }
}