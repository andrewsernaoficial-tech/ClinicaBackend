package pe.edu.upeu.ClinicaBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.ProcedimientoResponseDTO;
import pe.edu.upeu.ClinicaBackend.service.service.ProcedimientoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/procedimientos")
public class ProcedimientoController {

    private final ProcedimientoService procedimientoService;

    public ProcedimientoController(ProcedimientoService procedimientoService) {
        this.procedimientoService = procedimientoService;
    }

    @GetMapping
    public ResponseEntity<List<ProcedimientoResponseDTO>> listar() {
        return ResponseEntity.ok(procedimientoService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimientoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimientoService.read(id));
    }

    @PostMapping
    public ResponseEntity<ProcedimientoResponseDTO> registrar(@Valid @RequestBody ProcedimientoRequestDTO request) {
        return new ResponseEntity<>(procedimientoService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimientoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProcedimientoRequestDTO request) {
        return ResponseEntity.ok(procedimientoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        procedimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}