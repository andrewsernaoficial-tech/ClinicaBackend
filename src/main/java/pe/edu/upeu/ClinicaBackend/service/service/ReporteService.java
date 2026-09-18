package pe.edu.upeu.ClinicaBackend.service.service;

import pe.edu.upeu.ClinicaBackend.dto.reporte.AtencionPorEspecialidadDTO;
import pe.edu.upeu.ClinicaBackend.dto.reporte.ProcedimientoMasRealizadoDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    List<AtencionPorEspecialidadDTO> reporteAtencionesPorEspecialidad(LocalDate desde, LocalDate hasta);
    List<ProcedimientoMasRealizadoDTO> reporteProcedimientosMasRealizados(LocalDate desde, LocalDate hasta);
}