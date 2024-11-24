package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "voto")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long votanteId;

    private Long candidatoId; // Este campo puede ser `null` para el voto en blanco


    @Column(name = "eleccion_id", nullable = false)
    private Long eleccionId;

    // Getters y Setters (opcional si usas @Data de Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVotanteId() {
        return votanteId;
    }

    public void setVotanteId(Long votanteId) {
        this.votanteId = votanteId;
    }

    public Long getCandidatoId() {
        return candidatoId;
    }

    public void setCandidatoId(Long candidatoId) {
        this.candidatoId = candidatoId;
    }

    public Long getEleccionId() {
        return eleccionId;
    }

    public void setEleccionId(Long eleccionId) {
        this.eleccionId = eleccionId;
    }
}
