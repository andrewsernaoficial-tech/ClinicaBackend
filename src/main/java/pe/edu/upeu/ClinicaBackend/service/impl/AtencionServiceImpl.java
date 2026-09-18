package pe.edu.upeu.ClinicaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.ClinicaBackend.dto.AtencionRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.AtencionResponseDTO;
import pe.edu.upeu.ClinicaBackend.dto.DetalleAtencionRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.DetalleAtencionResponseDTO;
import pe.edu.upeu.ClinicaBackend.entity.Atencion;
import pe.edu.upeu.ClinicaBackend.entity.DetalleAtencion;
import pe.edu.upeu.ClinicaBackend.entity.Paciente;
import pe.edu.upeu.ClinicaBackend.entity.Procedimiento;
import pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion;
import pe.edu.upeu.ClinicaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.ClinicaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.ClinicaBackend.repository.AtencionRepository;
import pe.edu.upeu.ClinicaBackend.repository.PacienteRepository;
import pe.edu.upeu.ClinicaBackend.repository.ProcedimientoRepository;
import pe.edu.upeu.ClinicaBackend.service.service.AtencionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AtencionServiceImpl implements AtencionService {

    private static final Logger log = LoggerFactory.getLogger(AtencionServiceImpl.class);

    private final AtencionRepository atencionRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimientoRepository procedimientoRepository;

    public AtencionServiceImpl(AtencionRepository atencionRepository, PacienteRepository pacienteRepository, ProcedimientoRepository procedimientoRepository) {
        this.atencionRepository = atencionRepository;
        this.pacienteRepository = pacienteRepository;
        this.procedimientoRepository = procedimientoRepository;
    }

    @Override
    @Transactional
    public AtencionResponseDTO registrar(AtencionRequestDTO request) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando registro de atención. pacienteId={}, detalles={}", request.pacienteId(), request.detalles().size());

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + request.pacienteId()));

        if (!Boolean.TRUE.equals(paciente.getEstado())) {
            throw new ReglaNegocioException("No se puede registrar una atención para un paciente inactivo");
        }

        LocalDateTime hoyInicio = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime hoyFin = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        if (atencionRepository.existeAtencionMismoMedicoMismoDia(paciente.getId(), request.medicoTratante(), EstadoAtencion.REGISTRADA, hoyInicio, hoyFin)) {
            throw new ReglaNegocioException("El paciente ya tiene una atención registrada con este médico el día de hoy");
        }

        Atencion atencion = new Atencion();
        atencion.setPaciente(paciente);
        atencion.setFecha(LocalDateTime.now());
        atencion.setMedicoTratante(request.medicoTratante());
        atencion.setMotivoConsulta(request.motivoConsulta());
        atencion.setDiagnostico(request.diagnostico());
        atencion.setCodigoCie10(request.codigoCie10());
        atencion.setEstado(EstadoAtencion.REGISTRADA);

        BigDecimal total = BigDecimal.ZERO;
        Set<Long> procedimientosVistos = new HashSet<>();

        for (DetalleAtencionRequestDTO item : request.detalles()) {
            if (!procedimientosVistos.add(item.procedimientoId())) {
                throw new ReglaNegocioException("Un mismo procedimiento no puede repetirse en dos líneas de la misma atención");
            }

            Procedimiento procedimiento = procedimientoRepository.findById(item.procedimientoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Procedimiento no encontrado con id: " + item.procedimientoId()));

            if (!Boolean.TRUE.equals(procedimiento.getEstado())) {
                throw new ReglaNegocioException("El procedimiento " + procedimiento.getNombre() + " se encuentra inactivo");
            }

            BigDecimal subtotal = procedimiento.getTarifa().multiply(BigDecimal.valueOf(item.cantidad()));

            DetalleAtencion detalle = new DetalleAtencion();
            detalle.setProcedimiento(procedimiento);
            detalle.setCantidad(item.cantidad());
            detalle.setTarifa(procedimiento.getTarifa());
            detalle.setSubtotal(subtotal);

            atencion.agregarDetalle(detalle);
            total = total.add(subtotal);
        }

        atencion.setTotal(total);
        Atencion guardada = atencionRepository.save(atencion);

        log.info("Atención registrada con éxito. atencionId={}, total={}, duracionMs={}", guardada.getId(), total, System.currentTimeMillis() - inicio);
        return mapearDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public AtencionResponseDTO obtenerPorId(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Buscando atención con id: {}", id);
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Atención no encontrada con id: " + id));
        log.info("Atención encontrada. id={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(atencion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponseDTO> listar() {
        long inicio = System.currentTimeMillis();
        log.info("Listando todas las atenciones");
        List<AtencionResponseDTO> response = atencionRepository.findAll().stream().map(this::mapearDTO).toList();
        log.info("Listado completado. filas={}, duracionMs={}", response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    @Override
    @Transactional
    public AtencionResponseDTO anular(Long id) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando anulación de atención. atencionId={}", id);

        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Atención no encontrada con id: " + id));

        if (atencion.getEstado() == EstadoAtencion.ANULADA) {
            throw new ReglaNegocioException("La atención ya se encuentra ANULADA y no puede volver a anularse ni modificarse");
        }

        atencion.setEstado(EstadoAtencion.ANULADA);
        Atencion guardada = atencionRepository.save(atencion);

        log.info("Atención anulada con éxito. atencionId={}, duracionMs={}", id, System.currentTimeMillis() - inicio);
        return mapearDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponseDTO> obtenerHistoria(Long pacienteId) {
        long inicio = System.currentTimeMillis();
        log.info("Consultando historia clínica. pacienteId={}", pacienteId);

        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RecursoNoEncontradoException("Paciente no encontrado con id: " + pacienteId);
        }

        List<Atencion> atenciones = atencionRepository.findByPacienteIdOrderByFechaDesc(pacienteId);
        List<AtencionResponseDTO> response = atenciones.stream().map(this::mapearDTO).toList();

        log.info("Historia clínica obtenida. pacienteId={}, filas={}, duracionMs={}", pacienteId, response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponseDTO> buscar(Long pacienteId, EstadoAtencion estado, String medico, LocalDate desde, LocalDate hasta, String ordenarPor, String direccion) {
        long inicio = System.currentTimeMillis();
        log.info("Iniciando búsqueda de atenciones. pacienteId={}, estado={}, medico={}, desde={}, hasta={}, ordenarPor={}, direccion={}",
                pacienteId, estado, medico, desde, hasta, ordenarPor, direccion);

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha inicial no puede ser posterior a la fecha final");
        }

        LocalDateTime fechaDesde = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime fechaHasta = (hasta != null) ? hasta.atTime(LocalTime.MAX) : null;

        Set<String> camposPermitidos = Set.of("id", "fecha", "total", "estado");
        if (!camposPermitidos.contains(ordenarPor)) {
            throw new ReglaNegocioException("El campo de ordenamiento '" + ordenarPor + "' no está permitido");
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(direccion) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, ordenarPor);

        List<Atencion> atenciones = atencionRepository.buscarConFiltros(pacienteId, estado, medico, fechaDesde, fechaHasta, sort);
        List<AtencionResponseDTO> response = atenciones.stream().map(this::mapearDTO).toList();

        log.info("Búsqueda completada. filas={}, duracionMs={}", response.size(), System.currentTimeMillis() - inicio);
        return response;
    }

    private AtencionResponseDTO mapearDTO(Atencion atencion) {
        List<DetalleAtencionResponseDTO> detallesDTO = atencion.getDetalles().stream()
                .map(d -> new DetalleAtencionResponseDTO(
                        d.getProcedimiento().getId(),
                        d.getProcedimiento().getCodigo(),
                        d.getProcedimiento().getNombre(),
                        d.getCantidad(),
                        d.getTarifa(),
                        d.getSubtotal()
                )).toList();

        String pacienteNombre = atencion.getPaciente().getNombres() + " " + atencion.getPaciente().getApellidos();

        return new AtencionResponseDTO(
                atencion.getId(),
                atencion.getFecha(),
                atencion.getEstado().name(),
                atencion.getPaciente().getId(),
                pacienteNombre,
                atencion.getMedicoTratante(),
                atencion.getMotivoConsulta(),
                atencion.getDiagnostico(),
                atencion.getCodigoCie10(),
                atencion.getTotal(),
                detallesDTO
        );
    }
}