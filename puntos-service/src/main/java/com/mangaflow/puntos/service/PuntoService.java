package com.mangaflow.puntos.service;

import com.mangaflow.puntos.client.PagosClient;
import com.mangaflow.puntos.client.PersonasClient;
import com.mangaflow.puntos.dto.AcreditarPuntosRequest;
import com.mangaflow.puntos.dto.AcumularPuntosRequest;
import com.mangaflow.puntos.dto.CanjearPuntosRequest;
import com.mangaflow.puntos.dto.CuentaPuntosResponse;
import com.mangaflow.puntos.dto.MovimientoPuntosResponse;
import com.mangaflow.puntos.model.CuentaPuntos;
import com.mangaflow.puntos.model.MovimientoPuntos;
import com.mangaflow.puntos.model.TipoMovimiento;
import com.mangaflow.puntos.repository.CuentaPuntosRepository;
import com.mangaflow.puntos.repository.MovimientoPuntosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PuntoService {

    private final CuentaPuntosRepository cuentaRepository;
    private final MovimientoPuntosRepository movimientoRepository;
    private final PersonasClient personasClient;
    private final PagosClient pagosClient;

    public PuntoService(
            CuentaPuntosRepository cuentaRepository,
            MovimientoPuntosRepository movimientoRepository,
            PersonasClient personasClient,
            PagosClient pagosClient
    ) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.personasClient = personasClient;
        this.pagosClient = pagosClient;
    }

    public List<CuentaPuntosResponse> listarCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(this::toCuentaResponse)
                .toList();
    }

    public CuentaPuntosResponse obtenerCuentaPorPersona(Long personaId) {
        CuentaPuntos cuenta = buscarCuentaPorPersona(personaId);
        return toCuentaResponse(cuenta);
    }

    public List<MovimientoPuntosResponse> listarMovimientosPorPersona(Long personaId) {
        return movimientoRepository.findByPersonaId(personaId)
                .stream()
                .map(this::toMovimientoResponse)
                .toList();
    }

    public CuentaPuntosResponse acumularPuntos(AcumularPuntosRequest request) {

        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se pueden acumular puntos: la persona no existe");
        }

        if (!pagosClient.existePago(request.getPagoId())) {
            throw new IllegalArgumentException("No se pueden acumular puntos: el pago no existe");
        }

        PagosClient.PagoInfo pago = pagosClient.obtenerPago(request.getPagoId());

        if (pago == null) {
            throw new IllegalArgumentException("No se pudo obtener la información del pago");
        }

        if (!"APROBADO".equalsIgnoreCase(pago.getEstado())) {
            throw new IllegalArgumentException("No se pueden acumular puntos: el pago no está aprobado");
        }

        if (movimientoRepository.existsByPagoIdAndTipoMovimiento(request.getPagoId(), TipoMovimiento.GANANCIA_COMPRA)) {
            throw new IllegalArgumentException("Este pago ya generó puntos anteriormente");
        }

        int puntosGanados = calcularPuntos(pago.getMonto());

        CuentaPuntos cuenta = cuentaRepository.findByPersonaId(request.getPersonaId())
                .orElseGet(() -> crearCuenta(request.getPersonaId()));

        cuenta.setSaldo(cuenta.getSaldo() + puntosGanados);
        cuentaRepository.save(cuenta);

        MovimientoPuntos movimiento = MovimientoPuntos.builder()
                .personaId(request.getPersonaId())
                .pagoId(request.getPagoId())
                .tipoMovimiento(TipoMovimiento.GANANCIA_COMPRA)
                .puntos(puntosGanados)
                .descripcion("Puntos acumulados por compra aprobada")
                .fechaMovimiento(LocalDateTime.now())
                .build();

        movimientoRepository.save(movimiento);

        return toCuentaResponse(cuenta);
    }

    public CuentaPuntosResponse acreditarPuntos(AcreditarPuntosRequest request) {

        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se pueden acreditar puntos: la persona no existe");
        }

        CuentaPuntos cuenta = cuentaRepository.findByPersonaId(request.getPersonaId())
                .orElseGet(() -> crearCuenta(request.getPersonaId()));

        cuenta.setSaldo(cuenta.getSaldo() + request.getPuntos());
        cuentaRepository.save(cuenta);

        MovimientoPuntos movimiento = MovimientoPuntos.builder()
                .personaId(request.getPersonaId())
                .pagoId(null)
                .tipoMovimiento(TipoMovimiento.AJUSTE_ADMIN)
                .puntos(request.getPuntos())
                .descripcion(request.getDescripcion())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        movimientoRepository.save(movimiento);

        return toCuentaResponse(cuenta);
    }

    public CuentaPuntosResponse canjearPuntos(CanjearPuntosRequest request) {

        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se pueden canjear puntos: la persona no existe");
        }

        CuentaPuntos cuenta = buscarCuentaPorPersona(request.getPersonaId());

        if (cuenta.getSaldo() < request.getPuntos()) {
            throw new IllegalArgumentException("No se pueden canjear puntos: saldo insuficiente");
        }

        cuenta.setSaldo(cuenta.getSaldo() - request.getPuntos());
        cuentaRepository.save(cuenta);

        MovimientoPuntos movimiento = MovimientoPuntos.builder()
                .personaId(request.getPersonaId())
                .pagoId(null)
                .tipoMovimiento(TipoMovimiento.CANJE_RECOMPENSA)
                .puntos(request.getPuntos() * -1)
                .descripcion(request.getDescripcion())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        movimientoRepository.save(movimiento);

        return toCuentaResponse(cuenta);
    }

    public boolean existeCuentaPorPersona(Long personaId) {
        return cuentaRepository.existsByPersonaId(personaId);
    }

    public void eliminarCuenta(Long personaId) {
        CuentaPuntos cuenta = buscarCuentaPorPersona(personaId);
        cuenta.setActivo(false);
        cuentaRepository.save(cuenta);
    }

    private CuentaPuntos buscarCuentaPorPersona(Long personaId) {
        return cuentaRepository.findByPersonaId(personaId)
                .orElseThrow(() -> new RuntimeException("No existe cuenta de puntos para la persona con ID: " + personaId));
    }

    private CuentaPuntos crearCuenta(Long personaId) {
        CuentaPuntos cuenta = CuentaPuntos.builder()
                .personaId(personaId)
                .saldo(0)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        return cuentaRepository.save(cuenta);
    }

    private int calcularPuntos(Double monto) {
        return (int) (monto / 1000) * 10;
    }

    private CuentaPuntosResponse toCuentaResponse(CuentaPuntos cuenta) {
        return CuentaPuntosResponse.builder()
                .id(cuenta.getId())
                .personaId(cuenta.getPersonaId())
                .saldo(cuenta.getSaldo())
                .fechaCreacion(cuenta.getFechaCreacion())
                .activo(cuenta.getActivo())
                .build();
    }

    private MovimientoPuntosResponse toMovimientoResponse(MovimientoPuntos movimiento) {
        return MovimientoPuntosResponse.builder()
                .id(movimiento.getId())
                .personaId(movimiento.getPersonaId())
                .pagoId(movimiento.getPagoId())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .puntos(movimiento.getPuntos())
                .descripcion(movimiento.getDescripcion())
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .build();
    }
}