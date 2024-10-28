package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import co.ucentral.VotosSmart.persistencia.repositorios.VotoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VotoServicio {

    private final VotoRepositorio votoRepositorio;

    public Voto registrarVoto(Voto voto) {
        return votoRepositorio.save(voto);
    }

    public List<Voto> obtenerPorEleccionId(Long eleccionId) {
        return votoRepositorio.findByCandidatoEleccionId(eleccionId);
    }

    public List<Voto> obtenerTodos() {
        return (List<Voto>) votoRepositorio.findAll();
    }
    public Long contarVotosPorCandidato(Long candidatoId) {
        return votoRepositorio.countByCandidatoId(candidatoId);
    }
}
