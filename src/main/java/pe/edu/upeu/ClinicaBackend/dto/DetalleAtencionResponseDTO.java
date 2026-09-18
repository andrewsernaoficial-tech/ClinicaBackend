package pe.edu.upeu.ClinicaBackend.dto;

import java.math.BigDecimal;

public record DetalleAtencionResponseDTO(
        Long procedimientoId,
        String codigo,
        String nombre,
        Integer cantidad,
        BigDecimal tarifa,
        BigDecimal subtotal
) {}