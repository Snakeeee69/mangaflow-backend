package com.mangaflow.inventario.controller;

import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Inventario>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @GetMapping("/manga/{mangaId}")
    public ResponseEntity<Inventario> buscarPorMangaId(@PathVariable Long mangaId) {
        return ResponseEntity.ok(inventarioService.buscarPorMangaId(mangaId));
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@Valid @RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.crear(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Inventario inventario
    ) {
        return ResponseEntity.ok(inventarioService.actualizar(id, inventario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.ok("Registro de inventario eliminado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Inventario> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.desactivar(id));
    }

    @GetMapping("/disponible/{mangaId}/{cantidad}")
    public ResponseEntity<Boolean> hayStockDisponible(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.hayStockDisponible(mangaId, cantidad));
    }

    @PostMapping("/descontar/{mangaId}/{cantidad}")
    public ResponseEntity<Inventario> descontarStock(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.descontarStock(mangaId, cantidad));
    }

    @PostMapping("/agregar/{mangaId}/{cantidad}")
    public ResponseEntity<Inventario> agregarStock(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.agregarStock(mangaId, cantidad));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Inventario>> stockBajo() {
        return ResponseEntity.ok(inventarioService.stockBajo());
    }
}
