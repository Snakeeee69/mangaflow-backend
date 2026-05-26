package com.mangaflow.pagos.repository;

import com.mangaflow.pagos.model.EstadoPago;
import com.mangaflow.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByPedidoId(Long pedidoId);

    List<Pago> findByEstado(EstadoPago estado);

    List<Pago> findByActivoTrue();

    boolean existsByPedidoId(Long pedidoId);
}
