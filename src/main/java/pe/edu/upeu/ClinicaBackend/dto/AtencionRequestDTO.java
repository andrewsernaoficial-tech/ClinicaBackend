package pe.edu.upeu.ClinicaBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AtencionRequestDTO(
        @NotNull(message = "El paciente es obligatorio")
        @Positive(message = "El ID del paciente debe ser válido")
        Long pacienteId,

        @NotBlank(message = "El médico tratante es obligatorio")
        @Size(min = 3, max = 120, message = "El médico tratante debe tener entre 3 y 120 caracteres")
        String medicoTratante,

        @NotBlank(message = "El motivo de consulta es obligatorio")
        @Size(max = 300, message = "El motivo de consulta no puede exceder los 300 caracteres")
        String motivoConsulta,

        @NotBlank(message = "El diagnóstico es obligatorio")
        @Size(max = 500, message = "El diagnóstico no puede exceder los 500 caracteres")
        String diagnostico,

        @NotBlank(message = "El código CIE-10 es obligatorio")
        @Pattern(regexp = "^[A-Z]\\d{2}(\\.\\d{1,2})?$", message = "El código CIE-10 no tiene un formato válido")
        String codigoCie10,

        @NotEmpty(message = "La atención debe contener al menos un procedimiento")
        @Valid
        List<DetalleAtencionRequestDTO> detalles
) {}