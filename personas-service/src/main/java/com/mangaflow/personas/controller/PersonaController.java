package com.mangaflow.personas.controller;

import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listarTodas() {
        return ResponseEntity.ok(personaService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Persona>> listarActivas() {
        return ResponseEntity.ok(personaService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
        Persona nuevaPersona = personaService.crear(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Persona persona
    ) {
        return ResponseEntity.ok(personaService.actualizar(id, persona));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.ok("Persona eliminada correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Persona> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.desactivar(id));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Persona>> buscarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(personaService.buscarPorRol(rol));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existePersona(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.existePersona(id));
    }
}
