package com.mangaflow.catalogo.controller;

import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.service.MangaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/catalogo")
public class MangaController {

    private final MangaService mangaService;

    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @GetMapping
    public ResponseEntity<List<Manga>> listarTodos() {
        return ResponseEntity.ok(mangaService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Manga>> listarActivos() {
        return ResponseEntity.ok(mangaService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manga> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Manga> crear(@Valid @RequestBody Manga manga) {
        Manga nuevoManga = mangaService.crear(manga);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoManga);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manga> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Manga manga
    ) {
        return ResponseEntity.ok(mangaService.actualizar(id, manga));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        mangaService.eliminar(id);
        return ResponseEntity.ok("Manga eliminado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Manga> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.desactivar(id));
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Manga>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(mangaService.buscarPorTitulo(titulo));
    }

    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Manga>> buscarPorAutor(@RequestParam String autor) {
        return ResponseEntity.ok(mangaService.buscarPorAutor(autor));
    }

    @GetMapping("/buscar/genero/{genero}")
    public ResponseEntity<List<Manga>> buscarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(mangaService.buscarPorGenero(genero));
    }

    @GetMapping("/buscar/demografia/{demografia}")
    public ResponseEntity<List<Manga>> buscarPorDemografia(@PathVariable String demografia) {
        return ResponseEntity.ok(mangaService.buscarPorDemografia(demografia));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeManga(@PathVariable Long id) {
        return ResponseEntity.ok(mangaService.existeManga(id));
    }
}
