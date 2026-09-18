package pe.edu.upeu.ClinicaBackend.dto.reporte;

import java.math.BigDecimal;

public record ProcedimientoMasRealizadoDTO(
        Long procedimientoId,
        String codigo,
        String nombre,
        String especialidadNombre,
        Long cantidadRealizada,
        BigDecimal montoTotal
) {}