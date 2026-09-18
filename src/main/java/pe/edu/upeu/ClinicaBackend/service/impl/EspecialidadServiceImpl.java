package pe.edu.upeu.ClinicaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.ClinicaBackend.dto.EspecialidadRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.EspecialidadResponseDTO;
import pe.edu.upeu.ClinicaBackend.entity.Especialidad;
import pe.edu.upeu.ClinicaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.ClinicaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.ClinicaBackend.repository.EspecialidadRepository;
import pe.edu.upeu.ClinicaBackend.service.service.EspecialidadService;

import java.util.List;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    private static final Logger log = LoggerFactory.getLogger(EspecialidadServiceImpl.class);
    private final EspecialidadRepository especialidadRepository;

    public EspecialidadServiceImpl(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    @Transactional
    public EspecialidadResponseDTO create(EspecialidadRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando creación de especialidad: {}", request.nombre());

        if (especialidadRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ReglaNegocioException("Ya existe una especialidad con el nombre: " + request.nombre());
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(request.nombre());
        especialidad.setDescripcion(request.descripcion());
        especialidad.setEstado(request.estado());

        Especialidad guardada = especialidadRepository.save(especialidad);

        log.info("Especialidad creada con éxito. id={}, duracionMs={}", guardada.getId(), System.currentTimeMillis() - inicio);
        return mapearDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponseDTO read(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Buscando especialidad con id: {}", id);

        Especialidad especialidad = buscarPorId(id);

        log.info("Especialidad encontrada. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(especialidad);
    }

    @Override
    @Transactional
    public EspecialidadResponseDTO update(Long id, EspecialidadRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Actualizando especialidad con id: {}", id);

        Especialidad especialidad = buscarPorId(id);

        if (especialidadRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre(), id)) {
            throw new ReglaNegocioException("Ya existe otra especialidad con el nombre: " + request.nombre());
        }

        especialidad.setNombre(request.nombre());
        especialidad.setDescripcion(request.descripcion());
        especialidad.setEstado(request.estado());

        Especialidad actualizada = especialidadRepository.save(especialidad);

        log.info("Especialidad actualizada con éxito. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(actualizada);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Eliminando especialidad con id: {}", id);

        Especialidad especialidad = buscarPorId(id);

        try {
            especialidadRepository.delete(especialidad);
            log.info("Especialidad eliminada. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        } catch (Exception e) {
            throw new ReglaNegocioException("No se puede eliminar la especialidad porque tiene registros asociados");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponseDTO> readAll() {
        long inicio = System.currentTimeMillis();
        log.info("Listando todas las especialidades");

        List<Especialidad> lista = especialidadRepository.findAll();
        List<EspecialidadResponseDTO> response = lista.stream().map(this::mapearDTO).toList();

        log.info("Listado completado. filas={}, duracionMs={}", response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    private Especialidad buscarPorId(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con id: " + id));
    }

    private EspecialidadResponseDTO mapearDTO(Especialidad especialidad) {
        return new EspecialidadResponseDTO(
                especialidad.getId(),
                especialidad.getNombre(),
                especialidad.getDescripcion(),
                especialidad.getEstado(),
                especialidad.getFechaCreacion()
        );
    }
}