package com.mangaflow.personas.controller;

import com.mangaflow.personas.dto.PersonaRequest;
import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Personas", description = "Gestión de personas y roles de usuario")
@RestController
@RequestMapping("/mangas/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @Operation(summary = "Listar todas las personas")
    @GetMapping
    public ResponseEntity<List<PersonaResponse>> listarTodas() {
        return ResponseEntity.ok(personaService.listarTodas());
    }

    @Operation(summary = "Listar las personas activas")
    @GetMapping("/activas")
    public ResponseEntity<List<PersonaResponse>> listarActivas() {
        return ResponseEntity.ok(personaService.listarActivas());
    }

    @Operation(summary = "Buscar una persona por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarPorId(id));
    }

    @Operation(summary = "Crear una nueva persona")
    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody PersonaRequest request) {
        PersonaResponse nuevaPersona = personaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @Operation(summary = "Actualizar una persona existente")
    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaRequest request
    ) {
        return ResponseEntity.ok(personaService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar una persona")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.ok("Persona eliminada correctamente");
    }

    @Operation(summary = "Desactivar una persona")
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<PersonaResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.desactivar(id));
    }

    @Operation(summary = "Buscar personas por rol")
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<PersonaResponse>> buscarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(personaService.buscarPorRol(rol));
    }

    @Operation(summary = "Verificar si una persona existe por su ID")
    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existePersona(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.existePersona(id));
    }
}
