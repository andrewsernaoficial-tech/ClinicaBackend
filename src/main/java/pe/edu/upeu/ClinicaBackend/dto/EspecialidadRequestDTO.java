package pe.edu.upeu.ClinicaBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EspecialidadRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String nombre,

        @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
        String descripcion,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {}