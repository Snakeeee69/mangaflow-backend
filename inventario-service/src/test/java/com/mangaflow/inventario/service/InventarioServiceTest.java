package com.mangaflow.inventario.service;

import com.mangaflow.inventario.client.CatalogoClient;
import com.mangaflow.inventario.dto.InventarioRequest;
import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private InventarioService inventarioService;

    private InventarioRequest requestValido() {
        InventarioRequest request = new InventarioRequest();
        request.setMangaId(10L);
        request.setStock(50);
        request.setStockMinimo(5);
        return request;
    }

    private Inventario inventarioPersistido() {
        return Inventario.builder()
                .id(1L)
                .mangaId(10L)
                .stock(50)
                .stockMinimo(5)
                .activo(true)
                .build();
    }

    @Test
    void crear_cuandoMangaExisteYNoHayDuplicado_guarda() {
        when(catalogoClient.existeManga(10L)).thenReturn(true);
        when(inventarioRepository.existsByMangaId(10L)).thenReturn(false);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioPersistido());

        InventarioResponse response = inventarioService.crear(requestValido());

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStock()).isEqualTo(50);
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void crear_cuandoMangaNoExiste_lanzaIllegalArgument() {
        when(catalogoClient.existeManga(10L)).thenReturn(false);

        assertThatThrownBy(() -> inventarioService.crear(requestValido()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no existe en catálogo");

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void crear_cuandoYaExisteInventario_lanzaIllegalArgument() {
        when(catalogoClient.existeManga(10L)).thenReturn(true);
        when(inventarioRepository.existsByMangaId(10L)).thenReturn(true);

        assertThatThrownBy(() -> inventarioService.crear(requestValido()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe inventario");
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaRuntimeException() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventarioService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe registro de inventario con ID: 99");
    }

    @Test
    void descontarStock_cuandoStockSuficiente_descuenta() {
        when(inventarioRepository.findByMangaId(10L)).thenReturn(Optional.of(inventarioPersistido()));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

        InventarioResponse response = inventarioService.descontarStock(10L, 20);

        assertThat(response.getStock()).isEqualTo(30);
    }

    @Test
    void descontarStock_cuandoStockInsuficiente_lanzaIllegalArgument() {
        when(inventarioRepository.findByMangaId(10L)).thenReturn(Optional.of(inventarioPersistido()));

        assertThatThrownBy(() -> inventarioService.descontarStock(10L, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock insuficiente");

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void hayStockDisponible_devuelveTrueCuandoAlcanza() {
        when(inventarioRepository.findByMangaId(10L)).thenReturn(Optional.of(inventarioPersistido()));

        assertThat(inventarioService.hayStockDisponible(10L, 50)).isTrue();
        assertThat(inventarioService.hayStockDisponible(10L, 51)).isFalse();
    }
}
