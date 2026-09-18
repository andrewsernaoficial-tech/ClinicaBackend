package pe.edu.upeu.ClinicaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoResponseDTO;
import pe.edu.upeu.ClinicaBackend.entity.Especialidad;
import pe.edu.upeu.ClinicaBackend.entity.Procedimiento;
import pe.edu.upeu.ClinicaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.ClinicaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.ClinicaBackend.repository.EspecialidadRepository;
import pe.edu.upeu.ClinicaBackend.repository.ProcedimientoRepository;
import pe.edu.upeu.ClinicaBackend.service.service.ProcedimientoService;

import java.util.List;

@Service
public class ProcedimientoServiceImpl implements ProcedimientoService {

    private static final Logger log = LoggerFactory.getLogger(ProcedimientoServiceImpl.class);

    private final ProcedimientoRepository procedimientoRepository;
    private final EspecialidadRepository especialidadRepository;

    public ProcedimientoServiceImpl(ProcedimientoRepository procedimientoRepository, EspecialidadRepository especialidadRepository) {
        this.procedimientoRepository = procedimientoRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    @Transactional
    public ProcedimientoResponseDTO create(ProcedimientoRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando creación de procedimiento: {}", request.codigo());

        if (procedimientoRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ReglaNegocioException("Ya existe un procedimiento con el código: " + request.codigo());
        }

        Especialidad especialidad = especialidadRepository.findById(request.especialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con id: " + request.especialidadId()));

        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setCodigo(request.codigo());
        procedimiento.setNombre(request.nombre());
        procedimiento.setTarifa(request.tarifa());
        procedimiento.setDuracionMinutos(request.duracionMinutos());
        procedimiento.setEstado(request.estado());
        procedimiento.setEspecialidad(especialidad);

        Procedimiento guardado = procedimientoRepository.save(procedimiento);

        log.info("Procedimiento creado con éxito. id={}, duracionMs={}", guardado.getId(), System.currentTimeMillis() - inicio);
        return mapearDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProcedimientoResponseDTO read(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Buscando procedimiento con id: {}", id);

        Procedimiento procedimiento = buscarPorId(id);

        log.info("Procedimiento encontrado. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(procedimiento);
    }

    @Override
    @Transactional
    public ProcedimientoResponseDTO update(Long id, ProcedimientoRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Actualizando procedimiento con id: {}", id);

        Procedimiento procedimiento = buscarPorId(id);

        if (procedimientoRepository.existsByCodigoIgnoreCaseAndIdNot(request.codigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro procedimiento con el código: " + request.codigo());
        }

        Especialidad especialidad = especialidadRepository.findById(request.especialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con id: " + request.especialidadId()));

        procedimiento.setCodigo(request.codigo());
        procedimiento.setNombre(request.nombre());
        procedimiento.setTarifa(request.tarifa());
        procedimiento.setDuracionMinutos(request.duracionMinutos());
        procedimiento.setEstado(request.estado());
        procedimiento.setEspecialidad(especialidad);

        Procedimiento actualizado = procedimientoRepository.save(procedimiento);

        log.info("Procedimiento actualizado con éxito. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Eliminando procedimiento con id: {}", id);

        Procedimiento procedimiento = buscarPorId(id);

        try {
            procedimientoRepository.delete(procedimiento);
            log.info("Procedimiento eliminado. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        } catch (Exception e) {
            throw new ReglaNegocioException("No se puede eliminar el procedimiento porque tiene atenciones registradas");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcedimientoResponseDTO> readAll() {
        long inicio = System.currentTimeMillis();
        log.info("Listando todos los procedimientos");

        List<Procedimiento> lista = procedimientoRepository.findAll();
        List<ProcedimientoResponseDTO> response = lista.stream().map(this::mapearDTO).toList();

        log.info("Listado completado. filas={}, duracionMs={}", response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    private Procedimiento buscarPorId(Long id) {
        return procedimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Procedimiento no encontrado con id: " + id));
    }

    private ProcedimientoResponseDTO mapearDTO(Procedimiento procedimiento) {
        return new ProcedimientoResponseDTO(
                procedimiento.getId(),
                procedimiento.getCodigo(),
                procedimiento.getNombre(),
                procedimiento.getTarifa(),
                procedimiento.getDuracionMinutos(),
                procedimiento.getEstado(),
                procedimiento.getEspecialidad().getId(),
                procedimiento.getEspecialidad().getNombre(),
                procedimiento.getFechaCreacion()
        );
    }
}