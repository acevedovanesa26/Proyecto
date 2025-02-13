package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "elecciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Eleccion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eleccion_id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "fecha_fin", nullable = false)
    private Date fechaFin;

    @Column(name = "max_candidatos", nullable = false)
    private int maxCandidatos;

    @OneToMany(mappedBy = "eleccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Candidato> candidatos;

    // Campo transitorio para el estado
    @Transient
    private String estado;

    // Métodoquecalcula el estado en función de las fechas de inicio y fin
    public String getEstado() {
        Date now = new Date();
        if (now.before(this.fechaInicio)) {
            return "Pendiente";
        } else if (now.after(this.fechaFin)) {
            return "Finalizada";
        } else {
            return "En Curso";
        }
    }
}
