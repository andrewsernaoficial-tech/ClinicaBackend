package pe.edu.upeu.ClinicaBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleAtencionRequestDTO(
        @NotNull(message = "El procedimiento es obligatorio")
        @Positive(message = "El ID del procedimiento debe ser válido")
        Long procedimientoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        Integer cantidad
) {}