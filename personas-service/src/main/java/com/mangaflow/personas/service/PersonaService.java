package com.mangaflow.personas.service;

import com.mangaflow.personas.dto.PersonaRequest;
import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<PersonaResponse> listarTodas() {
        return personaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PersonaResponse> listarActivas() {
        return personaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PersonaResponse buscarPorId(Long id) {
        return toResponse(obtenerPersona(id));
    }

    public PersonaResponse crear(PersonaRequest request) {
        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una persona registrada con ese correo");
        }

        Persona persona = Persona.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .rol(request.getRol())
                .activo(request.getActivo() == null ? true : request.getActivo())
                .build();

        return toResponse(personaRepository.save(persona));
    }

    public PersonaResponse actualizar(Long id, PersonaRequest request) {
        Persona persona = obtenerPersona(id);

        if (!persona.getCorreo().equals(request.getCorreo())
                && personaRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está en uso por otra persona");
        }

        persona.setNombre(request.getNombre());
        persona.setCorreo(request.getCorreo());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
        persona.setRol(request.getRol());

        return toResponse(personaRepository.save(persona));
    }

    public void eliminar(Long id) {
        Persona persona = obtenerPersona(id);
        personaRepository.delete(persona);
    }

    public PersonaResponse desactivar(Long id) {
        Persona persona = obtenerPersona(id);
        persona.setActivo(false);
        return toResponse(personaRepository.save(persona));
    }

    public List<PersonaResponse> buscarPorRol(Rol rol) {
        return personaRepository.findByRol(rol)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean existePersona(Long id) {
        return personaRepository.existsById(id);
    }

    private Persona obtenerPersona(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una persona con el ID: " + id));
    }

    private PersonaResponse toResponse(Persona persona) {
        return PersonaResponse.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .correo(persona.getCorreo())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .rol(persona.getRol())
                .activo(persona.getActivo())
                .build();
    }
}
