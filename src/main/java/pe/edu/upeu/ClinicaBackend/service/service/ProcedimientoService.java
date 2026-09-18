package pe.edu.upeu.ClinicaBackend.service.service;

import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.generic.CrudService;

public interface ProcedimientoService extends CrudService<ProcedimientoRequestDTO, ProcedimientoResponseDTO, Long> {
}