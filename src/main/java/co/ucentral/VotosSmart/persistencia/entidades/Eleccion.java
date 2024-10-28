package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_fin", nullable = false)
    private Date fechaFin;

    @Column(name = "max_candidatos", nullable = false)
    private int maxCandidatos;

    @OneToMany(mappedBy = "eleccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidato> candidatos;
}
