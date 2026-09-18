package pe.edu.upeu.ClinicaBackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProcedimientoRequestDTO(
        @NotBlank(message = "El código es obligatorio")
        @Pattern(regexp = "^[A-Z0-9-]{3,10}$", message = "El código debe tener entre 3 y 10 caracteres alfanuméricos o guiones")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String nombre,

        @NotNull(message = "La tarifa es obligatoria")
        @DecimalMin(value = "0.01", message = "La tarifa debe ser mayor a cero")
        BigDecimal tarifa,

        @NotNull(message = "La duración es obligatoria")
        @Min(value = 5, message = "La duración mínima es de 5 minutos")
        Integer duracionMinutos,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado,

        @NotNull(message = "La especialidad es obligatoria")
        @Positive(message = "El ID de la especialidad debe ser válido")
        Long especialidadId
) {}