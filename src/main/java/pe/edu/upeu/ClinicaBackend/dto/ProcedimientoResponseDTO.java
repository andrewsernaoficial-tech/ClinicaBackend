package pe.edu.upeu.ClinicaBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProcedimientoResponseDTO(
        Long id,
        String codigo,
        String nombre,
        BigDecimal tarifa,
        Integer duracionMinutos,
        Boolean estado,
        Long especialidadId,
        String especialidadNombre,
        LocalDateTime fechaCreacion
) {}