package com.mangaflow.pagos.service;

import com.mangaflow.pagos.client.PedidosClient;
import com.mangaflow.pagos.dto.PagoRequest;
import com.mangaflow.pagos.dto.PagoResponse;
import com.mangaflow.pagos.model.EstadoPago;
import com.mangaflow.pagos.model.Pago;
import com.mangaflow.pagos.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidosClient pedidosClient;

    public PagoService(PagoRepository pagoRepository, PedidosClient pedidosClient) {
        this.pagoRepository = pagoRepository;
        this.pedidosClient = pedidosClient;
    }

    public List<PagoResponse> listarTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PagoResponse> listarActivos() {
        return pagoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Pago buscarEntidadPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un pago con ID: " + id));
    }

    public PagoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadPorId(id));
    }

    public List<PagoResponse> buscarPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PagoResponse crear(PagoRequest request) {

        if (!pedidosClient.existePedido(request.getPedidoId())) {
            throw new IllegalArgumentException("No se puede registrar el pago: el pedido no existe");
        }

        if (pagoRepository.existsByPedidoId(request.getPedidoId())) {
            throw new IllegalArgumentException("Ya existe un pago registrado para este pedido");
        }

        Pago pago = Pago.builder()
                .pedidoId(request.getPedidoId())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estado(EstadoPago.PENDIENTE)
                .fechaPago(LocalDateTime.now())
                .activo(true)
                .build();

        return toResponse(pagoRepository.save(pago));
    }

    public PagoResponse aprobar(Long id) {
        Pago pago = buscarEntidadPorId(id);

        if (pago.getEstado() == EstadoPago.RECHAZADO) {
            throw new IllegalArgumentException("No se puede aprobar un pago rechazado");
        }

        pago.setEstado(EstadoPago.APROBADO);
        return toResponse(pagoRepository.save(pago));
    }

    public PagoResponse rechazar(Long id) {
        Pago pago = buscarEntidadPorId(id);

        if (pago.getEstado() == EstadoPago.APROBADO) {
            throw new IllegalArgumentException("No se puede rechazar un pago aprobado");
        }

        pago.setEstado(EstadoPago.RECHAZADO);
        return toResponse(pagoRepository.save(pago));
    }

    public void eliminar(Long id) {
        Pago pago = buscarEntidadPorId(id);
        pagoRepository.delete(pago);
    }

    public boolean existePago(Long id) {
        return pagoRepository.existsById(id);
    }

    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .pedidoId(pago.getPedidoId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .activo(pago.getActivo())
                .build();
    }
}
