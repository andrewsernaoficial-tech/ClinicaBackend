package pe.edu.upeu.ClinicaBackend.service.service;

import pe.edu.upeu.ClinicaBackend.dto.AtencionRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.AtencionResponseDTO;
import pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion;

import java.time.LocalDate;
import java.util.List;

public interface AtencionService {
    AtencionResponseDTO registrar(AtencionRequestDTO request);
    AtencionResponseDTO obtenerPorId(Long id);
    List<AtencionResponseDTO> listar();
    AtencionResponseDTO anular(Long id);
    List<AtencionResponseDTO> obtenerHistoria(Long pacienteId);
    List<AtencionResponseDTO> buscar(Long pacienteId, EstadoAtencion estado, String medico, LocalDate desde, LocalDate hasta, String ordenarPor, String direccion);
}