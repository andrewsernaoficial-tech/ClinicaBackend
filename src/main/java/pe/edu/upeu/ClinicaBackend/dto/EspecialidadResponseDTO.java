package pe.edu.upeu.ClinicaBackend.dto;

import java.time.LocalDateTime;

public record EspecialidadResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        Boolean estado,
        LocalDateTime fechaCreacion
) {}