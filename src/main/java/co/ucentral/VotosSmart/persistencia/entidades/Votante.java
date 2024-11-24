package co.ucentral.VotosSmart.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Votante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoEstudiante;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String codigoAleatorio;

    @Column(nullable = false)
    private Boolean haVotado = false;

    private Long candidatoVotadoId;
    private Long eleccionId;

    // Getters y Setters adicionales (opcional si usas @Data de Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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
