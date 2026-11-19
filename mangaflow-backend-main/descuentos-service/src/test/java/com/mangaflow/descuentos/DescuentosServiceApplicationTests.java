package com.mangaflow.descuentos;

import com.mangaflow.descuentos.client.CatalogoClient;
import com.mangaflow.descuentos.client.PersonasClient;
import com.mangaflow.descuentos.dto.CalcularDescuentoRequest;
import com.mangaflow.descuentos.dto.DescuentoResponse;
import com.mangaflow.descuentos.model.Descuento;
import com.mangaflow.descuentos.model.TipoDescuento;
import com.mangaflow.descuentos.repository.DescuentoRepository;
import com.mangaflow.descuentos.service.DescuentoService;
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
class DescuentosServiceApplicationTests {

    @Mock
    private DescuentoRepository descuentoRepository;

    @Mock
    private PersonasClient personasClient;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private DescuentoService descuentoService;

    private Descuento descuentoMock;

    @BeforeEach
    void setUp() {
        descuentoMock = Descuento.builder()
                .id(1L)
                .personaId(10L)
                .mangaId(20L)
                .precioOriginal(15000.0)
                .porcentajeDescuento(15.0)
                .montoDescuento(2250.0)
                .precioFinal(12750.0)
                .tipoDescuento(TipoDescuento.PROMOCION)
                .motivo("Descuento por promoción activa")
                .fechaCalculo(LocalDateTime.now())
                .activo(true)
                .build();
    }

    @Test
    void listarDescuentos_retornaLista() {
        when(descuentoRepository.findAll()).thenReturn(List.of(descuentoMock));

        List<DescuentoResponse> resultado = descuentoService.listarDescuentos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(descuentoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_descuentoExiste_retornaDescuento() {
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(descuentoMock));

        DescuentoResponse response = descuentoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(descuentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> descuentoService.buscarPorId(99L));
    }

    @Test
    void calcularDescuento_personaYMangaValidos_guardaYRetorna() {
        CalcularDescuentoRequest request = new CalcularDescuentoRequest();
        request.setPersonaId(10L);
        request.setMangaId(20L);
        request.setPrecioOriginal(15000.0);
        request.setTipoDescuento(TipoDescuento.PROMOCION);

        when(personasClient.existePersona(10L)).thenReturn(true);
        when(catalogoClient.existeManga(20L)).thenReturn(true);
        when(descuentoRepository.save(any(Descuento.class))).thenReturn(descuentoMock);

        DescuentoResponse response = descuentoService.calcularDescuento(request);

        assertNotNull(response);
        verify(descuentoRepository).save(any(Descuento.class));
    }

    @Test
    void calcularDescuento_personaNoExiste_lanzaExcepcion() {
        CalcularDescuentoRequest request = new CalcularDescuentoRequest();
        request.setPersonaId(99L);
        request.setMangaId(20L);
        request.setPrecioOriginal(10000.0);
        request.setTipoDescuento(TipoDescuento.PREVENTA);

        when(personasClient.existePersona(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> descuentoService.calcularDescuento(request));
    }

    @Test
    void existeDescuento_retornaTrue() {
        when(descuentoRepository.existsByIdAndActivoTrue(1L)).thenReturn(true);

        assertTrue(descuentoService.existeDescuento(1L));
    }
}
