package com.mangaflow.catalogo.controller;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Catálogo", description = "Gestión del catálogo de mangas")
@RestController
@RequestMapping("/mangas/catalogo")
public class MangaController {

    private final MangaService mangaService;

    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @Operation(summary = "Listar todos los mangas")
    @GetMapping
    public ResponseEntity<List<MangaResponse>> listarTodos() {
        return ResponseEntity.ok(mangaService.listarTodos());
    }

    @Operation(summary = "Listar los mangas activos")
    @GetMapping("/activos")
    public ResponseEntity<List<MangaResponse>> listarActivos() {
        return ResponseEntity.ok(mangaService.listarActivos());
    }

    @Operation(summary = "Buscar un manga por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<MangaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.buscarPorId(id));
    }

    @Operation(summary = "Crear un nuevo manga")
    @PostMapping
    public ResponseEntity<MangaResponse> crear(@Valid @RequestBody MangaRequest request) {
        MangaResponse nuevoManga = mangaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoManga);
    }

    @Operation(summary = "Actualizar un manga existente")
    @PutMapping("/{id}")
    public ResponseEntity<MangaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MangaRequest request
    ) {
        return ResponseEntity.ok(mangaService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un manga")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        mangaService.eliminar(id);
        return ResponseEntity.ok("Manga eliminado correctamente");
    }

    @Operation(summary = "Desactivar un manga")
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<MangaResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.desactivar(id));
    }

    @Operation(summary = "Buscar mangas por título")
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<MangaResponse>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(mangaService.buscarPorTitulo(titulo));
    }

    @Operation(summary = "Buscar mangas por autor")
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<MangaResponse>> buscarPorAutor(@RequestParam String autor) {
        return ResponseEntity.ok(mangaService.buscarPorAutor(autor));
    }

    @Operation(summary = "Buscar mangas por género")
    @GetMapping("/buscar/genero/{genero}")
    public ResponseEntity<List<MangaResponse>> buscarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(mangaService.buscarPorGenero(genero));
    }

    @Operation(summary = "Buscar mangas por demografía")
    @GetMapping("/buscar/demografia/{demografia}")
    public ResponseEntity<List<MangaResponse>> buscarPorDemografia(@PathVariable String demografia) {
        return ResponseEntity.ok(mangaService.buscarPorDemografia(demografia));
    }

    @Operation(summary = "Verificar si un manga existe por su ID")
    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeManga(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.existeManga(id));
    }
}
