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
    private Long id;

    private String nombre;
    private String descripcion;

    @Column(name = "imagen_url") // Solo el nombre del archivo, no la ruta completa
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "eleccion_id", nullable = false)
    private Eleccion eleccion;
}
