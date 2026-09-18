package pe.edu.upeu.ClinicaBackend.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ClinicaBackend.dto.AtencionRequestDTO;
import pe.edu.upeu.ClinicaBackend.dto.AtencionResponseDTO;
import pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion;
import pe.edu.upeu.ClinicaBackend.service.service.AtencionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @PostMapping
    public ResponseEntity<AtencionResponseDTO> registrar(@Valid @RequestBody AtencionRequestDTO request) {
        return new ResponseEntity<>(atencionService.registrar(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AtencionResponseDTO>> listar() {
        return ResponseEntity.ok(atencionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtencionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.obtenerPorId(id));
    }

    @PutMapping("/{id}/anulacion")
    public ResponseEntity<AtencionResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.anular(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AtencionResponseDTO>> buscar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) EstadoAtencion estado,
            @RequestParam(required = false) String medico,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "fecha") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccion) {
        return ResponseEntity.ok(atencionService.buscar(pacienteId, estado, medico, desde, hasta, ordenarPor, direccion));
    }
}