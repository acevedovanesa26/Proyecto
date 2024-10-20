package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Votante;
import org.springframework.data.repository.CrudRepository;

public interface VotanteRepositorio extends CrudRepository<Votante, Long> {
    Votante findByCodigoEstudiante(String codigoEstudiante);
}
