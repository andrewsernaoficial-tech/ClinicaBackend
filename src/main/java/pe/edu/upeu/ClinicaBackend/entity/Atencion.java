package pe.edu.upeu.ClinicaBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.upeu.ClinicaBackend.enums.EstadoAtencion;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "atenciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "medico_tratante", nullable = false, length = 120)
    private String medicoTratante;

    @Column(name = "motivo_consulta", nullable = false, length = 300)
    private String motivoConsulta;

    @Column(nullable = false, length = 500)
    private String diagnostico;

    @Column(name = "codigo_cie10", nullable = false, length = 10)
    private String codigoCie10;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAtencion estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToMany(mappedBy = "atencion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleAtencion> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleAtencion d) {
        detalles.add(d);
        d.setAtencion(this);
    }
}