package pe.edu.upeu.ClinicaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ClinicaBackend.entity.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByDni(String dni);
    boolean existsByDniAndIdNot(String dni, Long id);
}