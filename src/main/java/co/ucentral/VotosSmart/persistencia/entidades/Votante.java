package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "votantes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Votante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "votante_id")
    private Long id;

    @Column(name = "codigo_estudiante", nullable = false, unique = true)
    private String codigoEstudiante;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "codigo_aleatorio", nullable = false, unique = true)
    private String codigoAleatorio;

    @ManyToOne
    @JoinColumn(name = "eleccion_id", nullable = false)
    private Eleccion eleccion;
}
