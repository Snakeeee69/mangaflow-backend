package com.mangaflow.catalogo.service;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.model.FormatoManga;
import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.repository.MangaRepository;
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
class MangaServiceTest {

    @Mock
    private MangaRepository mangaRepository;

    @InjectMocks
    private MangaService mangaService;

    private MangaRequest requestValido() {
        MangaRequest request = new MangaRequest();
        request.setTitulo("Berserk");
        request.setAutor("Kentaro Miura");
        request.setEditorial("Hakusensha");
        request.setGenero("Seinen");
        request.setDemografia("Adulto");
        request.setSinopsis("Un espadachín en un mundo oscuro");
        request.setNumeroTomo(1);
        request.setPrecioFisico(15000.0);
        request.setPrecioDigital(9000.0);
        request.setFormato(FormatoManga.FISICO_Y_DIGITAL);
        return request;
    }

    private Manga mangaPersistido() {
        return Manga.builder()
                .id(1L)
                .titulo("Berserk")
                .autor("Kentaro Miura")
                .editorial("Hakusensha")
                .genero("Seinen")
                .demografia("Adulto")
                .sinopsis("Un espadachín en un mundo oscuro")
                .numeroTomo(1)
                .precioFisico(15000.0)
                .precioDigital(9000.0)
                .formato(FormatoManga.FISICO_Y_DIGITAL)
                .activo(true)
                .build();
    }

    @Test
    void crear_cuandoNoExiste_guardaYRetornaResponse() {
        when(mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo("Berserk", 1)).thenReturn(false);
        when(mangaRepository.save(any(Manga.class))).thenReturn(mangaPersistido());

        MangaResponse response = mangaService.crear(requestValido());

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitulo()).isEqualTo("Berserk");
        assertThat(response.getActivo()).isTrue();
        verify(mangaRepository).save(any(Manga.class));
    }

    @Test
    void crear_cuandoYaExiste_lanzaIllegalArgument() {
        when(mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo("Berserk", 1)).thenReturn(true);

        assertThatThrownBy(() -> mangaService.crear(requestValido()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mismo número de tomo");

        verify(mangaRepository, never()).save(any(Manga.class));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaResponse() {
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(mangaPersistido()));

        MangaResponse response = mangaService.buscarPorId(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getAutor()).isEqualTo("Kentaro Miura");
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaRuntimeException() {
        when(mangaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mangaService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe un manga con el ID: 99");
    }

    @Test
    void listarTodos_retornaListaMapeada() {
        when(mangaRepository.findAll()).thenReturn(List.of(mangaPersistido()));

        List<MangaResponse> respuestas = mangaService.listarTodos();

        assertThat(respuestas).hasSize(1);
        assertThat(respuestas.get(0).getTitulo()).isEqualTo("Berserk");
    }

    @Test
    void desactivar_marcaInactivoYGuarda() {
        Manga manga = mangaPersistido();
        when(mangaRepository.findById(1L)).thenReturn(Optional.of(manga));
        when(mangaRepository.save(any(Manga.class))).thenReturn(manga);

        MangaResponse response = mangaService.desactivar(1L);

        assertThat(response.getActivo()).isFalse();
        verify(mangaRepository).save(manga);
    }
}
