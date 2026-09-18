package pe.edu.upeu.ClinicaBackend.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.ClinicaBackend.dto.reporte.AtencionPorEspecialidadDTO;
import pe.edu.upeu.ClinicaBackend.dto.reporte.ProcedimientoMasRealizadoDTO;
import pe.edu.upeu.ClinicaBackend.service.service.ReporteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/atenciones-por-especialidad")
    public ResponseEntity<List<AtencionPorEspecialidadDTO>> atencionesPorEspecialidad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(reporteService.reporteAtencionesPorEspecialidad(desde, hasta));
    }

    @GetMapping("/procedimientos-mas-realizados")
    public ResponseEntity<List<ProcedimientoMasRealizadoDTO>> procedimientosMasRealizados(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(reporteService.reporteProcedimientosMasRealizados(desde, hasta));
    }
}