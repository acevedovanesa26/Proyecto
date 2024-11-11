package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.persistencia.repositorios.CandidatoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CandidatoServicio {

    private final CandidatoRepositorio candidatoRepositorio;

    public List<Candidato> obtenerPorEleccionId(Long eleccionId) {
        return candidatoRepositorio.findByEleccionId(eleccionId);
    }

    public Candidato guardar(Candidato candidato) {
        return candidatoRepositorio.save(candidato);
    }

    public Candidato obtenerPorId(Long id) {
        return candidatoRepositorio.findById(id).orElse(null);
    }

    public boolean borrar(Candidato candidato) {
        try {
            candidatoRepositorio.delete(candidato);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}