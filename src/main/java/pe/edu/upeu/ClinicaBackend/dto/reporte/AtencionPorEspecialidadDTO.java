package pe.edu.upeu.ClinicaBackend.dto.reporte;

import java.math.BigDecimal;

public record AtencionPorEspecialidadDTO(
        Long especialidadId,
        String especialidadNombre,
        Long cantidadProcedimientos,
        BigDecimal montoTotal
) {}