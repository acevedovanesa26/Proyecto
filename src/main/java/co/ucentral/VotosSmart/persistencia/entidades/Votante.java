package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Votante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoEstudiante;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(unique = true, nullable = false)
    private String codigoAleatorio;
}
