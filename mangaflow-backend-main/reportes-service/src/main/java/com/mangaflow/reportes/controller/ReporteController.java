package com.mangaflow.reportes.controller;

import com.mangaflow.reportes.service.ReporteService;
import com.mangaflow.reportes.dto.ReporteGuardadoResponse;
import com.mangaflow.reportes.dto.ReporteResumenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/reportes")
@Tag(name = "Reportes", description = "Operaciones relacionadas con reportes")
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
