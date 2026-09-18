package pe.edu.upeu.ClinicaBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ClinicaBackend.dto.EspecialidadRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.EspecialidadResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.service.EspecialidadService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listar() {
        return ResponseEntity.ok(especialidadService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.read(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> registrar(@Valid @RequestBody EspecialidadRequestDTO request) {
        return new ResponseEntity<>(especialidadService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadRequestDTO request) {
        return ResponseEntity.ok(especialidadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}