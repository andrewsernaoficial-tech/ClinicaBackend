package pe.edu.upeu.ClinicaBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AtencionResponseDTO(
        Long id,
        LocalDateTime fecha,
        String estado,
        Long pacienteId,
        String pacienteNombre,
        String medicoTratante,
        String motivoConsulta,
        String diagnostico,
        String codigoCie10,
        BigDecimal total,
        List<DetalleAtencionResponseDTO> detalles
) {}