package pe.edu.upeu.ClinicaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ClinicaBackend.entity.Procedimiento;

public interface ProcedimientoRepository extends JpaRepository<Procedimiento, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
}