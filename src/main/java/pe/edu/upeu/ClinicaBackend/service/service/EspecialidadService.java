package pe.edu.upeu.ClinicaBackend.service.service;

import pe.edu.upeu.ClinicaBackend.dto.EspecialidadRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.EspecialidadResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.generic.CrudService;

public interface EspecialidadService extends CrudService<EspecialidadRequestDTO, EspecialidadResponseDTO, Long> {
}