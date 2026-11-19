package com.mangaflow.notificaciones;

import com.mangaflow.notificaciones.client.PersonasClient;
import com.mangaflow.notificaciones.dto.CrearNotificacionRequest;
import com.mangaflow.notificaciones.dto.NotificacionResponse;
import com.mangaflow.notificaciones.model.Notificacion;
import com.mangaflow.notificaciones.model.TipoNotificacion;
import com.mangaflow.notificaciones.repository.NotificacionRepository;
import com.mangaflow.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionesServiceApplicationTests {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private PersonasClient personasClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacionMock;

    @BeforeEach
    void setUp() {
        notificacionMock = Notificacion.builder()
                .id(1L)
                .personaId(10L)
                .titulo("Titulo de prueba")
                .mensaje("Mensaje de prueba")
                .tipoNotificacion(TipoNotificacion.PEDIDO)
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .fechaLectura(null)
                .activo(true)
                .build();
    }

    @Test
    void listarNotificaciones_retornaLista() {
        when(notificacionRepository.findByActivoTrue()).thenReturn(List.of(notificacionMock));

        List<NotificacionResponse> resultado = notificacionService.listarNotificaciones();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findByActivoTrue();
    }

    @Test
    void buscarPorId_existe_retornaNotificacion() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));

        NotificacionResponse response = notificacionService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificacionService.buscarPorId(99L));
    }

    @Test
    void crearNotificacion_personaValida_guardaYRetorna() {
        CrearNotificacionRequest request = new CrearNotificacionRequest();
        request.setPersonaId(10L);
        request.setTitulo("Titulo de prueba");
        request.setMensaje("Mensaje de prueba");
        request.setTipoNotificacion(TipoNotificacion.PEDIDO);

        when(personasClient.existePersona(10L)).thenReturn(true);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionMock);

        NotificacionResponse response = notificacionService.crearNotificacion(request);

        assertNotNull(response);
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void crearNotificacion_personaNoExiste_lanzaExcepcion() {
        CrearNotificacionRequest request = new CrearNotificacionRequest();
        request.setPersonaId(99L);

        when(personasClient.existePersona(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> notificacionService.crearNotificacion(request));
    }

    @Test
    void marcarComoLeida_notificacionNoLeida_marcaLeida() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionMock);

        NotificacionResponse response = notificacionService.marcarComoLeida(1L);

        assertNotNull(response);
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void existeNotificacion_retornaTrue() {
        when(notificacionRepository.existsByIdAndActivoTrue(1L)).thenReturn(true);

        assertTrue(notificacionService.existeNotificacion(1L));
    }
}
