package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import co.ucentral.VotosSmart.persistencia.repositorios.VotoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VotoServicio {

    private final VotoRepositorio votoRepositorio;

    public boolean registrarVoto(Long votanteId, Long candidatoId, Long eleccionId) {
        if (votoRepositorio.existsByVotanteIdAndEleccionId(votanteId, eleccionId)) {
            return false; // El votante ya ha votado en esta elección
        }

        Voto voto = new Voto();
        voto.setVotanteId(votanteId);
        voto.setCandidatoId(candidatoId);
        voto.setEleccionId(eleccionId);
        votoRepositorio.save(voto);
        return true;
    }
}
