package pe.edu.upeu.ClinicaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.ClinicaBackend.dto.reporte.AtencionPorEspecialidadDTO;
import pe.edu.upeu.ClinicaBackend.dto.reporte.ProcedimientoMasRealizadoDTO;
import pe.edu.upeu.ClinicaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.ClinicaBackend.repository.AtencionRepository;
import pe.edu.upeu.ClinicaBackend.service.service.ReporteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);
    private final AtencionRepository atencionRepository;

    public ReporteServiceImpl(AtencionRepository atencionRepository) {
        this.atencionRepository = atencionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionPorEspecialidadDTO> reporteAtencionesPorEspecialidad(LocalDate desde, LocalDate hasta) {
        long inicio = System.currentTimeMillis();
        log.info("Generando reporte de atenciones por especialidad. desde={}, hasta={}", desde, hasta);

        validarFechas(desde, hasta);
        LocalDateTime fechaDesde = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime fechaHasta = (hasta != null) ? hasta.atTime(LocalTime.MAX) : null;

        List<AtencionPorEspecialidadDTO> reporte = atencionRepository.reporteAtencionesPorEspecialidad(fechaDesde, fechaHasta);

        log.info("Reporte generado. filas={}, duracionMs={}", reporte.size(), System.currentTimeMillis() - inicio);
        return reporte;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcedimientoMasRealizadoDTO> reporteProcedimientosMasRealizados(LocalDate desde, LocalDate hasta) {
        long inicio = System.currentTimeMillis();
        log.info("Generando reporte de procedimientos más realizados. desde={}, hasta={}", desde, hasta);

        validarFechas(desde, hasta);
        LocalDateTime fechaDesde = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime fechaHasta = (hasta != null) ? hasta.atTime(LocalTime.MAX) : null;

        List<ProcedimientoMasRealizadoDTO> reporte = atencionRepository.reporteProcedimientosMasRealizados(fechaDesde, fechaHasta);

        log.info("Reporte generado. filas={}, duracionMs={}", reporte.size(), System.currentTimeMillis() - inicio);
        return reporte;
    }

    private void validarFechas(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha inicial no puede ser posterior a la fecha final");
        }
    }
}