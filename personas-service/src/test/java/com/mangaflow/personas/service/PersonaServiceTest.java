package com.mangaflow.personas.service;

import com.mangaflow.personas.dto.PersonaRequest;
import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.repository.PersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    private PersonaRequest requestValido() {
        PersonaRequest request = new PersonaRequest();
        request.setNombre("Vicente Donoso");
        request.setCorreo("vicente@mangaflow.cl");
        request.setTelefono("987654321");
        request.setDireccion("Calle Falsa 123");
        request.setRol(Rol.CLIENTE);
        return request;
    }

    private Persona personaPersistida() {
        return Persona.builder()
                .id(1L)
                .nombre("Vicente Donoso")
                .correo("vicente@mangaflow.cl")
                .telefono("987654321")
                .direccion("Calle Falsa 123")
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();
    }

    @Test
    void crear_cuandoCorreoNoExiste_guarda() {
        when(personaRepository.existsByCorreo("vicente@mangaflow.cl")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(personaPersistida());

        PersonaResponse response = personaService.crear(requestValido());

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCorreo()).isEqualTo("vicente@mangaflow.cl");
        assertThat(response.getActivo()).isTrue();
        verify(personaRepository).save(any(Persona.class));
    }

    @Test
    void crear_cuandoCorreoYaExiste_lanzaIllegalArgument() {
        when(personaRepository.existsByCorreo("vicente@mangaflow.cl")).thenReturn(true);

        assertThatThrownBy(() -> personaService.crear(requestValido()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ese correo");

        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaRuntimeException() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personaService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe una persona con el ID: 99");
    }

    @Test
    void actualizar_cuandoNuevoCorreoEnUso_lanzaIllegalArgument() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaPersistida()));
        when(personaRepository.existsByCorreo("otro@mangaflow.cl")).thenReturn(true);

        PersonaRequest request = requestValido();
        request.setCorreo("otro@mangaflow.cl");

        assertThatThrownBy(() -> personaService.actualizar(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya está en uso");
    }

    @Test
    void buscarPorRol_retornaListaMapeada() {
        when(personaRepository.findByRol(Rol.CLIENTE)).thenReturn(List.of(personaPersistida()));

        List<PersonaResponse> respuestas = personaService.buscarPorRol(Rol.CLIENTE);

        assertThat(respuestas).hasSize(1);
        assertThat(respuestas.get(0).getRol()).isEqualTo(Rol.CLIENTE);
    }

    @Test
    void desactivar_marcaInactivoYGuarda() {
        Persona persona = personaPersistida();
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        PersonaResponse response = personaService.desactivar(1L);

        assertThat(response.getActivo()).isFalse();
        verify(personaRepository).save(persona);
    }
}
