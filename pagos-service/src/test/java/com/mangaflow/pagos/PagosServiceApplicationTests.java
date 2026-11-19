package com.mangaflow.pagos;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mangaflow.pagos.dto.PagoRequest;
import com.mangaflow.pagos.dto.PagoResponse;
import com.mangaflow.pagos.model.Pago;
import com.mangaflow.pagos.model.EstadoPago;
import com.mangaflow.pagos.repository.PagoRepository;
import com.mangaflow.pagos.service.PagoService;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void testCrearPagoConMockito() {

        PagoRequest request = new PagoRequest();
        request.setPedidoId(1L);
        request.setMonto(2000.0);

        Pago pagoSimulado = new Pago();
        pagoSimulado.setId(50L);
        pagoSimulado.setEstado(EstadoPago.APROBADO);

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoSimulado);

        PagoResponse respuesta = pagoService.crear(request);

        assertNotNull(respuesta, "El servicio debería devolver una respuesta creada");
    }
}