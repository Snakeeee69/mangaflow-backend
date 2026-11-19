package com.mangaflow.personas.controller;

import com.mangaflow.personas.dto.PersonaRequest;
import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/personas")
@Tag(name = "Personas Controller", description = "Endpoints avanzados para la gestión de usuarios y personal de MangaFlow")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las personas", description = "Recupera un listado completo de todos los registros del sistema sin filtros de estado.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<PersonaResponse>> listarTodas() {
        return ResponseEntity.ok(personaService.listarTodas());
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar personas activas", description = "Retorna únicamente los usuarios que se encuentran con estado activo dentro de la plataforma.")
    @ApiResponse(responseCode = "200", description = "Listado de usuarios activos obtenido")
    public ResponseEntity<List<PersonaResponse>> listarActivas() {
        return ResponseEntity.ok(personaService.listarActivas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar persona por ID", description = "Obtiene los detalles de un usuario usando su ID único. Si no existe, dispara un manejo global de error 404.")
    @ApiResponse(responseCode = "200", description = "Persona encontrada con éxito")
    @ApiResponse(responseCode = "404", description = "El ID provisto no corresponde a ninguna persona registrada")
    public ResponseEntity<PersonaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva persona", description = "Valida los datos de entrada a nivel DTO y crea el usuario. Controla duplicados de correo electrónico.")
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo electrónico ya en uso")
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una persona", description = "Modifica la información existente de un registro validando que el correo nuevo no choque con otros.")
    @ApiResponse(responseCode = "200", description = "Usuario modificado correctamente")
    @ApiResponse(responseCode = "400", description = "Error de validación en los atributos o violación de reglas de negocio")
    @ApiResponse(responseCode = "404", description = "No se localizó al usuario con el ID especificado")
    public ResponseEntity<PersonaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaRequest request
    ) {
        return ResponseEntity.ok(personaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar físicamente una persona", description = "Remueve de forma permanente el registro del usuario de la base de datos.")
    @ApiResponse(responseCode = "204", description = "Persona eliminada exitosamente (Sin contenido en respuesta)")
    @ApiResponse(responseCode = "404", description = "No se puede eliminar porque el ID no existe")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar una persona (Borrado Lógico)", description = "Cambia el estado de una persona a inactivo en vez de eliminarlo físicamente, preservando la integridad de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado cambiado a inactivo exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el ID solicitado")
    })
    public ResponseEntity<PersonaResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.desactivar(id));
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Filtrar personas por Rol", description = "Busca y devuelve todas las personas asociadas a un rol específico (ADMINISTRADOR, CLIENTE, OPERADOR).")
    @ApiResponse(responseCode = "200", description = "Listado por rol filtrado con éxito")
    public ResponseEntity<List<PersonaResponse>> buscarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(personaService.buscarPorRol(rol));
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar existencia (Comunicación entre Microservicios)", description = "Endpoint de utilidad rápida pensado para que microservicios externos como 'Pedidos' verifiquen la existencia de un cliente.")
    @ApiResponse(responseCode = "200", description = "Consulta respondida (Retorna true o false)")
    public ResponseEntity<Boolean> existePersona(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.existePersona(id));
    }
}
