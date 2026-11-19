package com.mangaflow.descuentos.controller;

import com.mangaflow.descuentos.dto.CalcularDescuentoRequest;
import com.mangaflow.descuentos.dto.DescuentoResponse;
import com.mangaflow.descuentos.service.DescuentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/descuentos")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping
    public ResponseEntity<List<DescuentoResponse>> listarDescuentos() {
        return ResponseEntity.ok(descuentoService.listarDescuentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescuentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.buscarPorId(id));
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<DescuentoResponse>> buscarPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(descuentoService.buscarPorPersona(personaId));
    }

    @GetMapping("/manga/{mangaId}")
    public ResponseEntity<List<DescuentoResponse>> buscarPorManga(@PathVariable Long mangaId) {
        return ResponseEntity.ok(descuentoService.buscarPorManga(mangaId));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeDescuento(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.existeDescuento(id));
    }

    @PostMapping("/calcular")
    public ResponseEntity<DescuentoResponse> calcularDescuento(@Valid @RequestBody CalcularDescuentoRequest request) {
        DescuentoResponse response = descuentoService.calcularDescuento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDescuento(@PathVariable Long id) {
        descuentoService.eliminarDescuento(id);
        return ResponseEntity.ok("Descuento desactivado correctamente");
    }
}
