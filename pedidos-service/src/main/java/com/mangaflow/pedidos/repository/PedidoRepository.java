package com.mangaflow.pedidos.repository;

import com.mangaflow.pedidos.model.EstadoPedido;
import com.mangaflow.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByPersonaId(Long personaId);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByActivoTrue();
}
