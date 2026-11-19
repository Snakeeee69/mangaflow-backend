package com.mangaflow.reportes;

import com.mangaflow.reportes.client.PagoClient;
import com.mangaflow.reportes.client.PedidoClient;
import com.mangaflow.reportes.dto.PagoDTO;
import com.mangaflow.reportes.dto.PedidoDTO;
import com.mangaflow.reportes.dto.ReporteResumenResponse;
import com.mangaflow.reportes.repository.ReporteRepository;
import com.mangaflow.reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private PagoClient pagoClient;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void generarResumenEnVivo_cuentaCorrecta() {
        PedidoDTO p1 = new PedidoDTO();
        p1.setEstado("PENDIENTE");
        PedidoDTO p2 = new PedidoDTO();
        p2.setEstado("PAGADO");
        PedidoDTO p3 = new PedidoDTO();
        p3.setEstado("COMPLETADO");

        List<PedidoDTO> pedidos = List.of(p1, p2, p3);

        PagoDTO pay1 = new PagoDTO();
        pay1.setEstado("APROBADO");
        PagoDTO pay2 = new PagoDTO();
        pay2.setEstado("RECHAZADO");
        List<PagoDTO> pagos = List.of(pay1, pay2);

        when(pedidoClient.obtenerPedidos()).thenReturn(pedidos);
        when(pagoClient.obtenerPagos()).thenReturn(pagos);

        ReporteResumenResponse res = reporteService.generarResumenEnVivo();

        assertEquals(3, res.getTotalPedidos());
        assertEquals(1, res.getPedidosPendientes());
        assertEquals(2, res.getPedidosCompletados());

        assertEquals(2, res.getTotalPagos());
        assertEquals(1, res.getPagosAprobados());
        assertEquals(1, res.getPagosRechazados());
    }
}
