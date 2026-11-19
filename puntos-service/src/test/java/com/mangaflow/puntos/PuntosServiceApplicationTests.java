package com.mangaflow.puntos;

import com.mangaflow.puntos.client.PagosClient;
import com.mangaflow.puntos.client.PersonasClient;
import com.mangaflow.puntos.dto.AcumularPuntosRequest;
import com.mangaflow.puntos.dto.CanjearPuntosRequest;
import com.mangaflow.puntos.dto.CuentaPuntosResponse;
import com.mangaflow.puntos.model.CuentaPuntos;
import com.mangaflow.puntos.model.MovimientoPuntos;
import com.mangaflow.puntos.model.TipoMovimiento;
import com.mangaflow.puntos.repository.CuentaPuntosRepository;
import com.mangaflow.puntos.repository.MovimientoPuntosRepository;
import com.mangaflow.puntos.service.PuntoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PuntosServiceApplicationTests {

    @Mock
    private CuentaPuntosRepository cuentaRepository;

    @Mock
    private MovimientoPuntosRepository movimientoRepository;

    @Mock
    private PersonasClient personasClient;

    @Mock
    private PagosClient pagosClient;

    @InjectMocks
    private PuntoService puntoService;

    private CuentaPuntos cuentaMock;

    @BeforeEach
    void setUp() {
        cuentaMock = CuentaPuntos.builder()
                .id(1L)
                .personaId(10L)
                .saldo(100)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();
    }

    @Test
    void listarCuentas_retornaLista() {
        when(cuentaRepository.findAll()).thenReturn(List.of(cuentaMock));

        List<CuentaPuntosResponse> resultado = puntoService.listarCuentas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    void obtenerCuentaPorPersona_cuentaExiste_retornaCuenta() {
        when(cuentaRepository.findByPersonaId(10L)).thenReturn(Optional.of(cuentaMock));

        CuentaPuntosResponse response = puntoService.obtenerCuentaPorPersona(10L);

        assertNotNull(response);
        assertEquals(10L, response.getPersonaId());
    }

    @Test
    void acumularPuntos_personaYPagoValidos_acumulaCorrectamente() {
        AcumularPuntosRequest request = new AcumularPuntosRequest();
        request.setPersonaId(10L);
        request.setPagoId(5L);

        PagosClient.PagoInfo pagoInfo = new PagosClient.PagoInfo();
        pagoInfo.setEstado("APROBADO");
        pagoInfo.setMonto(10000.0);

        when(personasClient.existePersona(10L)).thenReturn(true);
        when(pagosClient.existePago(5L)).thenReturn(true);
        when(pagosClient.obtenerPago(5L)).thenReturn(pagoInfo);
        when(movimientoRepository.existsByPagoIdAndTipoMovimiento(5L, TipoMovimiento.GANANCIA_COMPRA)).thenReturn(false);
        when(cuentaRepository.findByPersonaId(10L)).thenReturn(Optional.of(cuentaMock));
        when(cuentaRepository.save(any(CuentaPuntos.class))).thenReturn(cuentaMock);
        when(movimientoRepository.save(any(MovimientoPuntos.class))).thenReturn(new MovimientoPuntos());

        CuentaPuntosResponse response = puntoService.acumularPuntos(request);

        assertNotNull(response);
        verify(cuentaRepository).save(any(CuentaPuntos.class));
    }

    @Test
    void canjearPuntos_saldoSuficiente_canjea() {
        CanjearPuntosRequest request = new CanjearPuntosRequest();
        request.setPersonaId(10L);
        request.setPuntos(50);
        request.setDescripcion("Canje de prueba");

        when(personasClient.existePersona(10L)).thenReturn(true);
        when(cuentaRepository.findByPersonaId(10L)).thenReturn(Optional.of(cuentaMock));
        when(cuentaRepository.save(any(CuentaPuntos.class))).thenReturn(cuentaMock);
        when(movimientoRepository.save(any(MovimientoPuntos.class))).thenReturn(new MovimientoPuntos());

        CuentaPuntosResponse response = puntoService.canjearPuntos(request);

        assertNotNull(response);
        verify(cuentaRepository).save(any(CuentaPuntos.class));
    }

    @Test
    void canjearPuntos_saldoInsuficiente_lanzaExcepcion() {
        CanjearPuntosRequest request = new CanjearPuntosRequest();
        request.setPersonaId(10L);
        request.setPuntos(500);

        when(personasClient.existePersona(10L)).thenReturn(true);
        when(cuentaRepository.findByPersonaId(10L)).thenReturn(Optional.of(cuentaMock));

        assertThrows(IllegalArgumentException.class, () -> puntoService.canjearPuntos(request));
    }

    @Test
    void existeCuentaPorPersona_retornaTrue() {
        when(cuentaRepository.existsByPersonaId(10L)).thenReturn(true);

        assertTrue(puntoService.existeCuentaPorPersona(10L));
    }
}
