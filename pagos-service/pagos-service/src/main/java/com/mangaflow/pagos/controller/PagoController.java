package com.mangaflow.pagos.controller;

import com.mangaflow.pagos.dto.PagoRequest;
import com.mangaflow.pagos.dto.PagoResponse;
import com.mangaflow.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PagoResponse>> listarActivos() {
        return ResponseEntity.ok(pagoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponse>> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.buscarPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(@Valid @RequestBody PagoRequest request) {
        PagoResponse response = pagoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<PagoResponse> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.aprobar(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<PagoResponse> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.rechazar(id));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existePago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.existePago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.ok("Pago eliminado correctamente");
    }
}
