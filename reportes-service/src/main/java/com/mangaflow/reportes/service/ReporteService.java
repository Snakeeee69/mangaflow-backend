package com.mangaflow.reportes.service;

import com.mangaflow.reportes.client.PagoClient;
import com.mangaflow.reportes.client.PedidoClient;
import com.mangaflow.reportes.dto.PagoDTO;
import com.mangaflow.reportes.dto.PedidoDTO;
import com.mangaflow.reportes.dto.ReporteGuardadoResponse;
import com.mangaflow.reportes.dto.ReporteResumenResponse;
import com.mangaflow.reportes.model.Reporte;
import com.mangaflow.reportes.model.TipoReporte;
import com.mangaflow.reportes.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PedidoClient pedidoClient;
    private final PagoClient pagoClient;

    public ReporteService(
            ReporteRepository reporteRepository,
            PedidoClient pedidoClient,
            PagoClient pagoClient
    ) {
        this.reporteRepository = reporteRepository;
        this.pedidoClient = pedidoClient;
        this.pagoClient = pagoClient;
    }

    public List<ReporteGuardadoResponse> listarReportes() {
        return reporteRepository.findByActivoTrue()
                .stream()
                .map(this::toGuardadoResponse)
                .toList();
    }

    public ReporteGuardadoResponse buscarPorId(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe reporte con ID: " + id));

        if (!reporte.getActivo()) {
            throw new RuntimeException("El reporte está desactivado");
        }

        return toGuardadoResponse(reporte);
    }

    public ReporteResumenResponse generarResumenEnVivo() {
        List<PedidoDTO> pedidos = pedidoClient.obtenerPedidos();
        List<PagoDTO> pagos = pagoClient.obtenerPagos();

        int totalPedidos = pedidos.size();

        int pedidosPendientes = (int) pedidos.stream()
                .filter(p -> p.getEstado() != null)
                .filter(p -> p.getEstado().equalsIgnoreCase("PENDIENTE"))
                .count();

        int pedidosCompletados = (int) pedidos.stream()
                .filter(p -> p.getEstado() != null)
                .filter(p ->
                        p.getEstado().equalsIgnoreCase("PAGADO") ||
                        p.getEstado().equalsIgnoreCase("COMPLETADO") ||
                        p.getEstado().equalsIgnoreCase("APROBADO")
                )
                .count();

        int totalPagos = pagos.size();

        int pagosAprobados = (int) pagos.stream()
                .filter(p -> p.getEstado() != null)
                .filter(p -> p.getEstado().equalsIgnoreCase("APROBADO"))
                .count();

        int pagosRechazados = (int) pagos.stream()
                .filter(p -> p.getEstado() != null)
                .filter(p -> p.getEstado().equalsIgnoreCase("RECHAZADO"))
                .count();

        double totalVendido = pagos.stream()
                .filter(p -> p.getEstado() != null)
                .filter(p -> p.getEstado().equalsIgnoreCase("APROBADO"))
                .mapToDouble(p -> p.getMonto() != null ? p.getMonto() : 0.0)
                .sum();

        return ReporteResumenResponse.builder()
                .totalPedidos(totalPedidos)
                .pedidosPendientes(pedidosPendientes)
                .pedidosCompletados(pedidosCompletados)
                .totalPagos(totalPagos)
                .pagosAprobados(pagosAprobados)
                .pagosRechazados(pagosRechazados)
                .totalVendido(totalVendido)
                .mensaje("Reporte generado correctamente desde pedidos-service y pagos-service")
                .build();
    }

    public ReporteGuardadoResponse generarYGuardarReporte() {
        ReporteResumenResponse resumen = generarResumenEnVivo();

        Reporte reporte = Reporte.builder()
                .tipoReporte(TipoReporte.RESUMEN_GENERAL)
                .totalPedidos(resumen.getTotalPedidos())
                .pedidosPendientes(resumen.getPedidosPendientes())
                .pedidosCompletados(resumen.getPedidosCompletados())
                .totalPagos(resumen.getTotalPagos())
                .pagosAprobados(resumen.getPagosAprobados())
                .pagosRechazados(resumen.getPagosRechazados())
                .totalVendido(resumen.getTotalVendido())
                .fechaGeneracion(LocalDateTime.now())
                .activo(true)
                .build();

        reporteRepository.save(reporte);

        return toGuardadoResponse(reporte);
    }

    public boolean existeReporte(Long id) {
        return reporteRepository.existsByIdAndActivoTrue(id);
    }

    public void eliminarReporte(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe reporte con ID: " + id));

        reporte.setActivo(false);
        reporteRepository.save(reporte);
    }

    private ReporteGuardadoResponse toGuardadoResponse(Reporte reporte) {
        return ReporteGuardadoResponse.builder()
                .id(reporte.getId())
                .tipoReporte(reporte.getTipoReporte())
                .totalPedidos(reporte.getTotalPedidos())
                .pedidosPendientes(reporte.getPedidosPendientes())
                .pedidosCompletados(reporte.getPedidosCompletados())
                .totalPagos(reporte.getTotalPagos())
                .pagosAprobados(reporte.getPagosAprobados())
                .pagosRechazados(reporte.getPagosRechazados())
                .totalVendido(reporte.getTotalVendido())
                .fechaGeneracion(reporte.getFechaGeneracion())
                .activo(reporte.getActivo())
                .build();
    }
}
