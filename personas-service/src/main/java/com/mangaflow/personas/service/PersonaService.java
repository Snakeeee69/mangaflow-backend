package com.mangaflow.personas.service;

import com.mangaflow.personas.dto.PersonaRequest;
import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.exception.BusinessRuleException;
import com.mangaflow.personas.exception.ResourceNotFoundException;
import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.repository.PersonaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    private PersonaResponse mapToResponse(Persona persona) {
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

    private Persona buscarEntidadPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una persona con el ID: " + id));
    }

    public List<PersonaResponse> listarTodas() {
        return personaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PersonaResponse> listarActivas() {
        return personaRepository.findByActivoTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PersonaResponse buscarPorId(Long id) {
        return mapToResponse(buscarEntidadPorId(id));
    }

    public PersonaResponse crear(@Valid PersonaRequest request) {
        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessRuleException("Ya existe una persona registrada con el correo: " + request.getCorreo());
        }

        Persona nuevaPersona = Persona.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .rol(request.getRol())
                .activo(true)
                .build();

        return mapToResponse(personaRepository.save(nuevaPersona));
    }

    public PersonaResponse actualizar(Long id, @Valid PersonaRequest request) {
        Persona persona = buscarEntidadPorId(id);

        if (!persona.getCorreo().equals(request.getCorreo())
                && personaRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessRuleException("El correo ya está en uso por otra persona");
        }

        persona.setNombre(request.getNombre());
        persona.setCorreo(request.getCorreo());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
        persona.setRol(request.getRol());

        return mapToResponse(personaRepository.save(persona));
    }

    public void eliminar(Long id) {
        Persona persona = buscarEntidadPorId(id);
        personaRepository.delete(persona);
    }

    public PersonaResponse desactivar(Long id) {
        Persona persona = buscarEntidadPorId(id);
        persona.setActivo(false);
        return mapToResponse(personaRepository.save(persona));
    }

    public List<PersonaResponse> buscarPorRol(Rol rol) {
        return personaRepository.findByRol(rol).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean existePersona(Long id) {
        return personaRepository.existsById(id);
    }
}
