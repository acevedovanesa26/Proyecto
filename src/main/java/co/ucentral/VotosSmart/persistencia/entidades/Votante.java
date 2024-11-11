package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;



@Entity
public class Votante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoAleatorio;
    private Boolean haVotado;

    private Long candidatoVotadoId;
    private Long eleccionId;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoAleatorio() {
        return codigoAleatorio;
    }

    public void setCodigoAleatorio(String codigoAleatorio) {
        this.codigoAleatorio = codigoAleatorio;
    }

    public Boolean getHaVotado() {
        return haVotado;
    }

    public void setHaVotado(Boolean haVotado) {
        this.haVotado = haVotado;
    }

    public Long getCandidatoVotadoId() {
        return candidatoVotadoId;
    }

    public void setCandidatoVotadoId(Long candidatoVotadoId) {
        this.candidatoVotadoId = candidatoVotadoId;
    }

    public Long getEleccionId() {
        return eleccionId;
    }

    public void setEleccionId(Long eleccionId) {
        this.eleccionId = eleccionId;
    }
}
