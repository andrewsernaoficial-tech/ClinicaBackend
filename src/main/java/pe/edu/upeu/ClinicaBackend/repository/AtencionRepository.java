package pe.edu.upeu.ClinicaBackend.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.ClinicaBackend.entity.Atencion;
import pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion;

import java.time.LocalDateTime;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Atencion a WHERE a.paciente.id = :pacienteId AND a.estado = :estado AND lower(a.medicoTratante) = lower(:medico) AND a.fecha >= :inicioDia AND a.fecha <= :finDia")
    boolean existeAtencionMismoMedicoMismoDia(
            @Param("pacienteId") Long pacienteId,
            @Param("medico") String medico,
            @Param("estado") EstadoAtencion estado,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia);

    List<Atencion> findByPacienteIdOrderByFechaDesc(Long pacienteId);

    @Query("SELECT DISTINCT a FROM Atencion a " +
            "LEFT JOIN FETCH a.paciente " +
            "LEFT JOIN FETCH a.detalles d " +
            "LEFT JOIN FETCH d.procedimiento " +
            "WHERE (:pacienteId IS NULL OR a.paciente.id = :pacienteId) " +
            "AND (:estado IS NULL OR a.estado = :estado) " +
            "AND (:medico IS NULL OR lower(a.medicoTratante) LIKE lower(concat('%', :medico, '%'))) " +
            "AND (cast(:desde as timestamp) IS NULL OR a.fecha >= :desde) " +
            "AND (cast(:hasta as timestamp) IS NULL OR a.fecha <= :hasta)")
    List<Atencion> buscarConFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("estado") EstadoAtencion estado,
            @Param("medico") String medico,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Sort sort);
    @Query("SELECT new pe.edu.upeu.ClinicaBackend.dto.reporte.AtencionPorEspecialidadDTO(" +
            "e.id, e.nombre, SUM(d.cantidad), SUM(d.subtotal)) " +
            "FROM DetalleAtencion d " +
            "JOIN d.procedimiento p " +
            "JOIN p.especialidad e " +
            "JOIN d.atencion a " +
            "WHERE a.estado = pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion.REGISTRADA " +
            "AND (cast(:desde as timestamp) IS NULL OR a.fecha >= :desde) " +
            "AND (cast(:hasta as timestamp) IS NULL OR a.fecha <= :hasta) " +
            "GROUP BY e.id, e.nombre " +
            "ORDER BY SUM(d.subtotal) DESC")
    List<pe.edu.upeu.ClinicaBackend.dto.reporte.AtencionPorEspecialidadDTO> reporteAtencionesPorEspecialidad(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    @Query("SELECT new pe.edu.upeu.ClinicaBackend.dto.reporte.ProcedimientoMasRealizadoDTO(" +
            "p.id, p.codigo, p.nombre, e.nombre, SUM(d.cantidad), SUM(d.subtotal)) " +
            "FROM DetalleAtencion d " +
            "JOIN d.procedimiento p " +
            "JOIN p.especialidad e " +
            "JOIN d.atencion a " +
            "WHERE a.estado = pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion.REGISTRADA " +
            "AND (cast(:desde as timestamp) IS NULL OR a.fecha >= :desde) " +
            "AND (cast(:hasta as timestamp) IS NULL OR a.fecha <= :hasta) " +
            "GROUP BY p.id, p.codigo, p.nombre, e.nombre " +
            "ORDER BY SUM(d.cantidad) DESC")
    List<pe.edu.upeu.ClinicaBackend.dto.reporte.ProcedimientoMasRealizadoDTO> reporteProcedimientosMasRealizados(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}