package com.mangaflow.inventario.controller;

import com.mangaflow.inventario.dto.InventarioRequest;
import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mangas/inventario")
@Tag(name = "Inventario Controller", description = "Endpoints de control de almacén, reabastecimiento y auditoría de stock")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los registros", description = "Recupera la totalidad de las existencias sin filtrar el estado activo.")
    @ApiResponse(responseCode = "200", description = "Listado de inventario obtenido con éxito")
    public ResponseEntity<List<InventarioResponse>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar inventarios activos", description = "Obtiene los mangas que están disponibles para la venta en almacén.")
    @ApiResponse(responseCode = "200", description = "Listado de existencias activas obtenido")
    public ResponseEntity<List<InventarioResponse>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID de Inventario", description = "Recupera los datos de stock físico de un registro por su ID principal.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro localizado"),
        @ApiResponse(responseCode = "404", description = "ID de inventario inexistente")
    })
    public ResponseEntity<InventarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @GetMapping("/manga/{mangaId}")
    @Operation(summary = "Buscar por ID de Manga", description = "Permite a otros servicios consultar las unidades de un manga pasándole su ID.")
    @ApiResponse(responseCode = "200", description = "Stock de manga localizado")
    public ResponseEntity<InventarioResponse> buscarPorMangaId(@PathVariable Long mangaId) {
        return ResponseEntity.ok(inventarioService.buscarPorMangaId(mangaId));
    }

    @PostMapping
    @Operation(summary = "Dar de alta un stock inicial", description = "Crea un espacio de inventario exclusivo para un nuevo manga.")
    @ApiResponse(responseCode = "201", description = "Inventario inicial guardado correctamente")
    public ResponseEntity<InventarioResponse> crear(@Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrado físico", description = "Remueve permanentemente el registro de control de stock de la base de datos.")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.ok("Registro de inventario eliminado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Borrado lógico", description = "Desactiva el registro del almacén para prevenir que sea comprado.")
    public ResponseEntity<InventarioResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.desactivar(id));
    }

    @GetMapping("/disponible/{mangaId}/{cantidad}")
    @Operation(summary = "Verificar disponibilidad", description = "Endpoint estratégico para que el servicio de Pedidos valide en tiempo real si hay suficientes copias antes de procesar un pago.")
    @ApiResponse(responseCode = "200", description = "Respuesta booleana calculada con éxito")
    public ResponseEntity<Boolean> hayStockDisponible(@PathVariable Long mangaId, @PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.hayStockDisponible(mangaId, cantidad));
    }

    @PostMapping("/descontar/{mangaId}/{cantidad}")
    @Operation(summary = "Descontar stock de forma automática", description = "Disminuye el stock disponible debido a una venta concretada.")
    public ResponseEntity<InventarioResponse> descontarStock(@PathVariable Long mangaId, @PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.descontarStock(mangaId, cantidad));
    }

    @PostMapping("/agregar/{mangaId}/{cantidad}")
    @Operation(summary = "Reabastecer stock por entrada", description = "Suma unidades físicas al inventario (ej: llegada de nuevos tomos del proveedor).")
    public ResponseEntity<InventarioResponse> agregarStock(@PathVariable Long mangaId, @PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.agregarStock(mangaId, cantidad));
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Alertas de stock crítico", description = "Devuelve una lista con todos los mangas que se encuentran en peligro de quiebre de stock (stock <= stockMinimo).")
    public ResponseEntity<List<InventarioResponse>> stockBajo() {
        return ResponseEntity.ok(inventarioService.stockBajo());
    }
}
