package com.mangaflow.personas.service;

import com.mangaflow.personas.dto.PersonaResponse;
import com.mangaflow.personas.exception.ResourceNotFoundException;
import com.mangaflow.personas.model.Persona;
import com.mangaflow.personas.model.Rol;
import com.mangaflow.personas.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService; 
    private Persona personaEjemplo;

    @BeforeEach
    void setUp() {
        personaEjemplo = Persona.builder()
                .id(1L)
                .nombre("Juan Perez")
                .correo("juan@mangaflow.com")
                .telefono("987654321")
                .direccion("Av. Siempre Viva 742")
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();
    }

    @Test
    void buscarPorId_CuandoExiste_DebeRetornarPersonaResponse() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaEjemplo));

        PersonaResponse resultado = personaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("juan@mangaflow.com", resultado.getCorreo());
        
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            personaService.buscarPorId(99L);
        });

        verify(personaRepository, times(1)).findById(99L);
    }
}
