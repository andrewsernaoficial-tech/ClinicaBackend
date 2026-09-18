package pe.edu.upeu.ClinicaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ClinicaBackend.entity.DetalleAtencion;

public interface DetalleAtencionRepository extends JpaRepository<DetalleAtencion, Long> {
}