package com.mangaflow.catalogo;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.exception.BusinessRuleException;
import com.mangaflow.catalogo.exception.ResourceNotFoundException;
import com.mangaflow.catalogo.model.FormatoManga;
import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.repository.MangaRepository;
import com.mangaflow.catalogo.service.MangaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MangaServiceTest {

    @Mock
    private MangaRepository mangaRepository;

    @InjectMocks
    private MangaService mangaService;

    private Manga mangaMock;
    private MangaRequest requestMock;

    @BeforeEach
    void setUp() {
        mangaMock = Manga.builder()
                .id(1L)
                .titulo("Chainsaw Man")
                .autor("Tatsuki Fujimoto")
                .editorial("Ivrea")
                .genero("Accion")
                .demografia("Shonen")
                .sinopsis("Pochita se vuelve un corazon")
                .numeroTomo(1)
                .precioFisico(11.50)
                .precioDigital(4.99)
                .formato(FormatoManga.FISICO_Y_DIGITAL)
                .activo(true)
                .build();

        requestMock = MangaRequest.builder()
                .titulo("Chainsaw Man")
                .autor("Tatsuki Fujimoto")
                .editorial("Ivrea")
                .genero("Accion")
                .demografia("Shonen")
                .sinopsis("Pochita se vuelve un corazon")
                .numeroTomo(1)
                .precioFisico(11.50)
                .precioDigital(4.99)
                .formato(FormatoManga.FISICO_Y_DIGITAL)
                .build();
    }

    @Test
    void listarTodos_DebeRetornarListaDeMangaResponse() {
        when(mangaRepository.findAll()).thenReturn(List.of(mangaMock));

        List<MangaResponse> resultado = mangaService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Chainsaw Man", resultado.get(0).getTitulo());
        verify(mangaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorIdDto_CuandoExisteManga_DebeRetornarMangaResponse() {
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(mangaMock));

        MangaResponse resultado = mangaService.buscarPorIdDto(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Chainsaw Man", resultado.getTitulo());
    }

    @Test
    void buscarPorIdDto_CuandoNoExisteManga_DebeLanzarResourceNotFoundException() {
        when(mangaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mangaService.buscarPorIdDto(99L));
    }

    @Test
    void crear_CuandoMangaNoEstaDuplicado_DebeGuardarYRetornarMangaResponse() {
        when(mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo("Chainsaw Man", 1)).thenReturn(false);
        when(mangaRepository.save(any(Manga.class))).thenReturn(mangaMock);

        MangaResponse resultado = mangaService.crear(requestMock);

        assertNotNull(resultado);
        assertEquals("Chainsaw Man", resultado.getTitulo());
        assertTrue(resultado.getActivo());
        verify(mangaRepository, times(1)).save(any(Manga.class));
    }

    @Test
    void crear_CuandoMangaYaExiste_DebeLanzarBusinessRuleException() {
        when(mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo("Chainsaw Man", 1)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> mangaService.crear(requestMock));
        
        verify(mangaRepository, never()).save(any(Manga.class));
    }

    @Test
    void desactivar_CuandoExisteManga_DebeCambiarEstadoAFlase() {
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(mangaMock));
        mangaMock.setActivo(false);
        when(mangaRepository.save(any(Manga.class))).thenReturn(mangaMock);

        MangaResponse resultado = mangaService.desactivar(1L);

        assertNotNull(resultado);
        assertFalse(resultado.getActivo());
    }

    @Test
    void eliminar_CuandoExisteManga_DebeLlamarMetodoDelete() {
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(mangaMock));
        doNothing().when(mangaRepository).delete(mangaMock);

        mangaService.eliminar(1L);

        verify(mangaRepository, times(1)).delete(mangaMock);
    }
}
