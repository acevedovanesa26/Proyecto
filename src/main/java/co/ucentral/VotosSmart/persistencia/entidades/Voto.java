package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Data
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long votanteId;

    private Long candidatoId; // Este campo puede ser `null` para el voto en blanco

    @Column(nullable = false)
    private Long eleccionId; // Identifica la elección específica a la que pertenece el voto

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
