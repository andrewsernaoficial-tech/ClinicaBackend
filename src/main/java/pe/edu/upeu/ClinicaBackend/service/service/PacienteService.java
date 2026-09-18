package pe.edu.upeu.ClinicaBackend.service.service;

import pe.edu.upeu.ClinicaBackend.dto.PacienteRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.PacienteResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.generic.CrudService;

public interface PacienteService extends CrudService<PacienteRequestDTO, PacienteResponseDTO, Long> {
}