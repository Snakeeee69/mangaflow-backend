package com.mangaflow.catalogo.controller;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/catalogo")
@Tag(name = "Catálogo Controller", description = "API para la gestión, mantenimiento y consultas avanzadas del catálogo general de mangas")
public class MangaController {

    private final MangaService mangaService;

    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @GetMapping
    @Operation(summary = "Listar todo el catálogo", description = "Retorna una lista completa con todos los mangas guardados en el sistema, incluyendo tanto los activos como los inactivos.")
    public ResponseEntity<List<MangaResponse>> listarTodos() {
        return ResponseEntity.ok(mangaService.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar mangas activos", description = "Retorna únicamente aquellos mangas que están marcados como activos y listos para la venta/visualización en el catálogo público.")
    public ResponseEntity<List<MangaResponse>> listarActivos() {
        return ResponseEntity.ok(mangaService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar manga por ID", description = "Obtiene los detalles completos de un manga especifico utilizando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manga encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID proporcionado no pertenece a ningún manga en la base de datos")
    })
    public ResponseEntity<MangaResponse> buscarPorId(
            @Parameter(description = "ID único del manga", example = "1") 
            @PathVariable Long id) {
        return ResponseEntity.ok(mangaService.buscarPorIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Dar de alta un nuevo manga", description = "Registra un nuevo manga en el catálogo global. Lanza un error si ya existe un registro con el mismo título y número de tomo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Manga registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados o regla de negocio duplicada")
    })
    public ResponseEntity<MangaResponse> crear(@Valid @RequestBody MangaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mangaService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un manga", description = "Modifica las propiedades de un manga existente localizándolo a través de su ID.")
    public ResponseEntity<MangaResponse> actualizar(
            @Parameter(description = "ID del manga que deseas modificar", example = "1") @PathVariable Long id, 
            @Valid @RequestBody MangaRequest request) {
        return ResponseEntity.ok(mangaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar físicamente un manga", description = "Remueve permanentemente el registro del manga de la base de datos mediante su ID.")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del manga a borrar de forma definitiva", example = "2") @PathVariable Long id) {
        mangaService.eliminar(id);
        return ResponseEntity.ok("Manga eliminado correctamente del catálogo");
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un manga (Eliminación lógica)", description = "Cambia el estado de disponibilidad del manga a 'inactivo' (false). El registro permanece en la base de datos pero se oculta de las búsquedas estándar.")
    public ResponseEntity<MangaResponse> desactivar(
            @Parameter(description = "ID del manga a dar de baja lógicamente", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mangaService.desactivar(id));
    }

    @PutMapping("/{id}/activar")
    @Operation(summary = "Reactivar un manga", description = "Cambia el estado de disponibilidad del manga a 'activo' (true) para volver a mostrarlo en el catálogo público.")
    public ResponseEntity<MangaResponse> activar(
            @Parameter(description = "ID del manga a reactivar", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mangaService.activar(id));
    }

    @GetMapping("/buscar/titulo")
    @Operation(summary = "Buscar mangas por título", description = "Realiza una consulta parcial (coincidencia de texto) en los títulos de los mangas sin importar mayúsculas o minúsculas.")
    public ResponseEntity<List<MangaResponse>> buscarPorTitulo(
            @Parameter(description = "Palabra clave o título completo a buscar", example = "Naruto") @RequestParam String titulo) {
        return ResponseEntity.ok(mangaService.buscarPorTitulo(titulo));
    }

    @GetMapping("/buscar/autor")
    @Operation(summary = "Buscar mangas por autor", description = "Filtra el catálogo y devuelve los mangas asociados al nombre o apellido del autor especificado.")
    public ResponseEntity<List<MangaResponse>> buscarPorAutor(
            @Parameter(description = "Nombre o fragmento del nombre del autor", example = "Eiichiro Oda") @RequestParam String autor) {
        return ResponseEntity.ok(mangaService.buscarPorAutor(autor));
    }

    @GetMapping("/buscar/genero/{genero}")
    @Operation(summary = "Filtrar por género", description = "Busca todos los mangas que pertenezcan a una categoría o género específico.")
    public ResponseEntity<List<MangaResponse>> buscarPorGenero(
            @Parameter(description = "Género del manga", example = "Accion") @PathVariable String genero) {
        return ResponseEntity.ok(mangaService.buscarPorGenero(genero));
    }

    @GetMapping("/buscar/demografia/{demografia}")
    @Operation(summary = "Filtrar por demografía", description = "Agrupa y retorna los mangas según su sector demográfico objetivo (ej: Shonen, Seinen, Shojo).")
    public ResponseEntity<List<MangaResponse>> buscarPorDemografia(
            @Parameter(description = "Demografía asignada al manga", example = "Shonen") @PathVariable String demografia) {
        return ResponseEntity.ok(mangaService.buscarPorDemografia(demografia));
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar existencia de un manga", description = "Devuelve un valor booleano directo (true/false) para comprobar si el manga existe en el catálogo. Este endpoint es clave para la comunicación interna con el microservicio de Inventario.")
    public ResponseEntity<Boolean> existeManga(
            @Parameter(description = "ID del manga del cual se quiere verificar existencia", example = "5") @PathVariable Long id) {
        return ResponseEntity.ok(mangaService.existeManga(id));
    }
}