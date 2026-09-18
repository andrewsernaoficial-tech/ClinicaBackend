package pe.edu.upeu.ClinicaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.ClinicaBackend.dto.PacienteRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.PacienteResponseDTO;
import pe.edu.upeu.ClinicaBackend.entity.Paciente;
import pe.edu.upeu.ClinicaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.ClinicaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.ClinicaBackend.repository.PacienteRepository;
import pe.edu.upeu.ClinicaBackend.service.service.PacienteService;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    private static final Logger log = LoggerFactory.getLogger(PacienteServiceImpl.class);
    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional
    public PacienteResponseDTO create(PacienteRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando creación de paciente con DNI: {}", request.dni());

        if (pacienteRepository.existsByDni(request.dni())) {
            throw new ReglaNegocioException("Ya existe un paciente con el DNI: " + request.dni());
        }

        Paciente paciente = new Paciente();
        paciente.setDni(request.dni());
        paciente.setNombres(request.nombres());
        paciente.setApellidos(request.apellidos());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(request.sexo());
        paciente.setEmail(request.email());
        paciente.setTelefono(request.telefono());
        paciente.setDireccion(request.direccion());
        paciente.setEstado(request.estado());

        Paciente guardado = pacienteRepository.save(paciente);

        log.info("Paciente creado con éxito. id={}, duracionMs={}", guardado.getId(), System.currentTimeMillis() - inicio);
        return mapearDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDTO read(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Buscando paciente con id: {}", id);

        Paciente paciente = buscarPorId(id);

        log.info("Paciente encontrado. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(paciente);
    }

    @Override
    @Transactional
    public PacienteResponseDTO update(Long id, PacienteRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Actualizando paciente con id: {}", id);

        Paciente paciente = buscarPorId(id);

        if (pacienteRepository.existsByDniAndIdNot(request.dni(), id)) {
            throw new ReglaNegocioException("Ya existe otro paciente con el DNI: " + request.dni());
        }

        paciente.setDni(request.dni());
        paciente.setNombres(request.nombres());
        paciente.setApellidos(request.apellidos());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(request.sexo());
        paciente.setEmail(request.email());
        paciente.setTelefono(request.telefono());
        paciente.setDireccion(request.direccion());
        paciente.setEstado(request.estado());

        Paciente actualizado = pacienteRepository.save(paciente);

        log.info("Paciente actualizado con éxito. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Eliminando paciente con id: {}", id);

        Paciente paciente = buscarPorId(id);

        try {
            pacienteRepository.delete(paciente);
            log.info("Paciente eliminado. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        } catch (Exception e) {
            throw new ReglaNegocioException("No se puede eliminar el paciente porque tiene atenciones registradas");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> readAll() {
        long inicio = System.currentTimeMillis();
        log.info("Listando todos los pacientes");

        List<Paciente> lista = pacienteRepository.findAll();
        List<PacienteResponseDTO> response = lista.stream().map(this::mapearDTO).toList();

        log.info("Listado completado. filas={}, duracionMs={}", response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    private Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id));
    }

    private PacienteResponseDTO mapearDTO(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getDni(),
                paciente.getNombres(),
                paciente.getApellidos(),
                paciente.getFechaNacimiento(),
                paciente.getSexo(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDireccion(),
                paciente.getEstado(),
                paciente.getFechaCreacion()
        );
    }
}