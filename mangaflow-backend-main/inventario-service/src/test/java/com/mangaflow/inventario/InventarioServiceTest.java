package com.mangaflow.inventario;

import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.exception.BusinessRuleException;
import com.mangaflow.inventario.exception.ResourceNotFoundException;
import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.repository.InventarioRepository;
import com.mangaflow.inventario.service.InventarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventarioMock;

    @BeforeEach
    void setUp() {
        inventarioMock = Inventario.builder()
                .id(1L)
                .mangaId(50L)
                .stock(30)
                .stockMinimo(5)
                .activo(true)
                .build();
    }

    @Test
    void cuandoDescontarStockEsExitoso_entoncesCalculaYGuarda() {
        Mockito.when(inventarioRepository.findByMangaId(50L)).thenReturn(Optional.of(inventarioMock));
        Mockito.when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> i.getArgument(0));

        InventarioResponse response = inventarioService.descontarStock(50L, 10);

        assertNotNull(response);
        assertEquals(20, response.getStock());
        Mockito.verify(inventarioRepository, Mockito.times(1)).save(any(Inventario.class));
    }

    @Test
    void cuandoDescontarMasDeLoDisponible_entoncesLanzaBusinessRuleException() {
        Mockito.when(inventarioRepository.findByMangaId(50L)).thenReturn(Optional.of(inventarioMock));

        assertThrows(BusinessRuleException.class, () -> {
            inventarioService.descontarStock(50L, 40);
        });

        Mockito.verify(inventarioRepository, Mockito.never()).save(any(Inventario.class));
    }

    @Test
    void cuandoSeBuscaMangaInexistente_entoncesLanzaResourceNotFoundException() {
        Mockito.when(inventarioRepository.findByMangaId(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            inventarioService.buscarPorMangaId(999L);
        });
    }
}
