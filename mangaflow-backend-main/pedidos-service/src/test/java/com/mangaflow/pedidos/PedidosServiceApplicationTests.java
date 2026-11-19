package com.mangaflow.pedidos;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mangaflow.pedidos.client.CatalogoClient;
import com.mangaflow.pedidos.client.InventarioClient;
import com.mangaflow.pedidos.client.PersonasClient;
import com.mangaflow.pedidos.dto.PedidoRequest;
import com.mangaflow.pedidos.dto.PedidoResponse;
import com.mangaflow.pedidos.model.Pedido;
import com.mangaflow.pedidos.repository.PedidoRepository;
import com.mangaflow.pedidos.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PersonasClient personasClient;

    @Mock
    private CatalogoClient catalogoClient;

    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void testCrearPedidoConMockito() {
        PedidoRequest request = new PedidoRequest();
        request.setPersonaId(1L);
        request.setMangaId(2L);
        request.setCantidad(3);
        request.setPrecioUnitario(10.5);

        Pedido pedidoSimulado = new Pedido();
        pedidoSimulado.setId(10L);
        pedidoSimulado.setPersonaId(1L);
        pedidoSimulado.setMangaId(2L);
        pedidoSimulado.setCantidad(3);
        pedidoSimulado.setPrecioUnitario(10.5);
        pedidoSimulado.setTotal(31.5);

        when(personasClient.existePersona(1L)).thenReturn(true);
        when(catalogoClient.existeManga(2L)).thenReturn(true);
        when(inventarioClient.hayStockDisponible(2L, 3)).thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSimulado);

        PedidoResponse respuesta = pedidoService.crear(request);

        assertNotNull(respuesta, "El servicio debería retornar un objeto no nulo");
    }
}