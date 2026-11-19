package com.mangaflow.pedidos.service;

import com.mangaflow.pedidos.client.CatalogoClient;
import com.mangaflow.pedidos.client.InventarioClient;
import com.mangaflow.pedidos.client.PersonasClient;
import com.mangaflow.pedidos.dto.PedidoRequest;
import com.mangaflow.pedidos.dto.PedidoResponse;
import com.mangaflow.pedidos.model.EstadoPedido;
import com.mangaflow.pedidos.model.Pedido;
import com.mangaflow.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PersonasClient personasClient;
    private final CatalogoClient catalogoClient;
    private final InventarioClient inventarioClient;

    public PedidoService(
            PedidoRepository pedidoRepository,
            PersonasClient personasClient,
            CatalogoClient catalogoClient,
            InventarioClient inventarioClient
    ) {
        this.pedidoRepository = pedidoRepository;
        this.personasClient = personasClient;
        this.catalogoClient = catalogoClient;
        this.inventarioClient = inventarioClient;
    }

    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PedidoResponse> listarActivos() {
        return pedidoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Pedido buscarEntidadPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un pedido con ID: " + id));
    }

    public PedidoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadPorId(id));
    }

    public List<PedidoResponse> buscarPorPersona(Long personaId) {
        return pedidoRepository.findByPersonaId(personaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PedidoResponse crear(PedidoRequest request) {
        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se puede crear el pedido: la persona no existe");
        }

        if (!catalogoClient.existeManga(request.getMangaId())) {
            throw new IllegalArgumentException("No se puede crear el pedido: el manga no existe");
        }

        if (!inventarioClient.hayStockDisponible(request.getMangaId(), request.getCantidad())) {
            throw new IllegalArgumentException("No se puede crear el pedido: stock insuficiente");
        }

        Double total = request.getPrecioUnitario() * request.getCantidad();

        Pedido pedido = Pedido.builder()
                .personaId(request.getPersonaId())
                .mangaId(request.getMangaId())
                .cantidad(request.getCantidad())
                .precioUnitario(request.getPrecioUnitario())
                .total(total)
                .estado(EstadoPedido.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        inventarioClient.descontarStock(request.getMangaId(), request.getCantidad());

        return toResponse(pedidoGuardado);
    }

    public PedidoResponse confirmar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new IllegalArgumentException("No se puede confirmar un pedido cancelado");
        }

        pedido.setEstado(EstadoPedido.CONFIRMADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    public PedidoResponse cancelar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);

        if (pedido.getEstado() == EstadoPedido.CONFIRMADO) {
            throw new IllegalArgumentException("No se puede cancelar un pedido confirmado");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    public void eliminar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);
        pedidoRepository.delete(pedido);
    }

    public boolean existePedido(Long id) {
        return pedidoRepository.existsById(id);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .personaId(pedido.getPersonaId())
                .mangaId(pedido.getMangaId())
                .cantidad(pedido.getCantidad())
                .precioUnitario(pedido.getPrecioUnitario())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .fechaCreacion(pedido.getFechaCreacion())
                .activo(pedido.getActivo())
                .build();
    }
}
