package com.mangaflow.inventario.controller;

import com.mangaflow.inventario.dto.InventarioRequest;
import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventario", description = "Gestión de stock e inventario de mangas")
@RestController
@RequestMapping("/mangas/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Listar todo el inventario")
    @GetMapping
    public ResponseEntity<List<InventarioResponse>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @Operation(summary = "Listar el inventario activo")
    @GetMapping("/activos")
    public ResponseEntity<List<InventarioResponse>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @Operation(summary = "Buscar un registro de inventario por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @Operation(summary = "Buscar inventario por ID de manga")
    @GetMapping("/manga/{mangaId}")
    public ResponseEntity<InventarioResponse> buscarPorMangaId(@PathVariable Long mangaId) {
        return ResponseEntity.ok(inventarioService.buscarPorMangaId(mangaId));
    }

    @Operation(summary = "Registrar un nuevo inventario")
    @PostMapping
    public ResponseEntity<InventarioResponse> crear(@Valid @RequestBody InventarioRequest request) {
        InventarioResponse nuevoInventario = inventarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
    }

    @Operation(summary = "Actualizar un registro de inventario")
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequest request
    ) {
        return ResponseEntity.ok(inventarioService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un registro de inventario")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.ok("Registro de inventario eliminado correctamente");
    }

    @Operation(summary = "Desactivar un registro de inventario")
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<InventarioResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.desactivar(id));
    }

    @Operation(summary = "Verificar si hay stock disponible para un manga")
    @GetMapping("/disponible/{mangaId}/{cantidad}")
    public ResponseEntity<Boolean> hayStockDisponible(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.hayStockDisponible(mangaId, cantidad));
    }

    @Operation(summary = "Descontar stock de un manga")
    @PostMapping("/descontar/{mangaId}/{cantidad}")
    public ResponseEntity<InventarioResponse> descontarStock(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.descontarStock(mangaId, cantidad));
    }

    @Operation(summary = "Agregar stock a un manga")
    @PostMapping("/agregar/{mangaId}/{cantidad}")
    public ResponseEntity<InventarioResponse> agregarStock(
            @PathVariable Long mangaId,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.agregarStock(mangaId, cantidad));
    }

    @Operation(summary = "Listar mangas con stock bajo el mínimo")
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<InventarioResponse>> stockBajo() {
        return ResponseEntity.ok(inventarioService.stockBajo());
    }
}
