package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import co.ucentral.VotosSmart.persistencia.repositorios.VotoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class VotoServicio {

    private final VotoRepositorio votoRepositorio;

    // Registrar voto en la base de datos
    @Transactional
    public void registrarVoto(Long votanteId, Long candidatoId, Long eleccionId) {
        if (!votanteHaVotado(votanteId, eleccionId)) {
            Voto voto = new Voto();
            voto.setVotanteId(votanteId);
            voto.setCandidatoId(candidatoId);
            voto.setEleccionId(eleccionId);
            votoRepositorio.save(voto);
        } else {
            throw new IllegalArgumentException("El votante ya ha votado en esta elección.");
        }
    }

    // Verificar si el votante ya ha votado en una elección específica
    public boolean votanteHaVotado(Long votanteId, Long eleccionId) {
        return votoRepositorio.existsByVotanteIdAndEleccionId(votanteId, eleccionId);
    }
}
