package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotoRepositorio extends CrudRepository<Voto, Long> {
    List<Voto> findByCandidatoEleccionId(Long eleccionId);
    Long countByCandidatoId(Long candidatoId);
}