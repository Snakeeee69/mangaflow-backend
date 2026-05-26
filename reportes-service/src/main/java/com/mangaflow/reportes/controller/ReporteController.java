package com.mangaflow.reportes.controller;

import com.mangaflow.reportes.dto.ReporteGuardadoResponse;
import com.mangaflow.reportes.dto.ReporteResumenResponse;
import com.mangaflow.reportes.service.ReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mangas/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<?> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteGuardadoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ReporteResumenResponse> generarResumenEnVivo() {
        return ResponseEntity.ok(reporteService.generarResumenEnVivo());
    }

    @PostMapping("/generar")
    public ResponseEntity<ReporteGuardadoResponse> generarYGuardarReporte() {
        ReporteGuardadoResponse response = reporteService.generarYGuardarReporte();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeReporte(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.existeReporte(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.ok("Reporte desactivado correctamente");
    }
}
