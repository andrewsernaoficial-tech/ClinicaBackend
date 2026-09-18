package pe.edu.upeu.ClinicaBackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteResponseDTO(
        Long id,
        String dni,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String sexo,
        String email,
        String telefono,
        String direccion,
        Boolean estado,
        LocalDateTime fechaCreacion
) {}