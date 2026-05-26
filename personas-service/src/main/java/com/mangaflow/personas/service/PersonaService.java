package com.mangaflow.personas.service;

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

    public List<Persona> listarTodas() {
        return personaRepository.findAll();
    }

    public List<Persona> listarActivas() {
        return personaRepository.findByActivoTrue();
    }

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una persona con el ID: " + id));
    }

    public Persona crear(Persona persona) {
        if (personaRepository.existsByCorreo(persona.getCorreo())) {
            throw new RuntimeException("Ya existe una persona registrada con ese correo");
        }

        if (persona.getActivo() == null) {
            persona.setActivo(true);
        }

        return personaRepository.save(persona);
    }

    public Persona actualizar(Long id, Persona datosNuevos) {
        Persona persona = buscarPorId(id);

        if (!persona.getCorreo().equals(datosNuevos.getCorreo())
                && personaRepository.existsByCorreo(datosNuevos.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso por otra persona");
        }

        persona.setNombre(datosNuevos.getNombre());
        persona.setCorreo(datosNuevos.getCorreo());
        persona.setTelefono(datosNuevos.getTelefono());
        persona.setDireccion(datosNuevos.getDireccion());
        persona.setRol(datosNuevos.getRol());

        return personaRepository.save(persona);
    }

    public void eliminar(Long id) {
        Persona persona = buscarPorId(id);
        personaRepository.delete(persona);
    }

    public Persona desactivar(Long id) {
        Persona persona = buscarPorId(id);
        persona.setActivo(false);
        return personaRepository.save(persona);
    }

    public List<Persona> buscarPorRol(Rol rol) {
        return personaRepository.findByRol(rol);
    }

    public boolean existePersona(Long id) {
        return personaRepository.existsById(id);
    }
}
