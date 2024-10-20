package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CandidatoRepositorio extends CrudRepository<Candidato, Long> {
    List<Candidato> findByEleccionId(Long eleccionId);
}

