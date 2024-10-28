package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidato_id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "eleccion_id", nullable = false)
    private Eleccion eleccion;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long numeroVotos;

    // Getter y Setter manuales para numeroVotos
    public Long getNumeroVotos() {
        return numeroVotos;
    }

    public void setNumeroVotos(Long numeroVotos) {
        this.numeroVotos = numeroVotos;
    }
}
