package pe.edu.upeu.ClinicaBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ClinicaBackend.dto.AtencionResponseDTO;
import pe.edu.upeu.ClinicaBackend.dto.PacienteRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.PacienteResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.service.AtencionService;
import pe.edu.upeu.ClinicaBackend.service.service.PacienteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;
    private final AtencionService atencionService;

    public PacienteController(PacienteService pacienteService, AtencionService atencionService) {
        this.pacienteService = pacienteService;
        this.atencionService = atencionService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listar() {
        return ResponseEntity.ok(pacienteService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.read(id));
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> registrar(@Valid @RequestBody PacienteRequestDTO request) {
        return new ResponseEntity<>(pacienteService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO request) {
        return ResponseEntity.ok(pacienteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historia")
    public ResponseEntity<List<AtencionResponseDTO>> obtenerHistoria(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.obtenerHistoria(id));
    }
}