package pe.edu.upeu.ClinicaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ClinicaBackend.entity.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}