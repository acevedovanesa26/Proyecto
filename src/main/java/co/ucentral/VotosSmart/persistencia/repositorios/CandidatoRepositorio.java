package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidatoRepositorio extends JpaRepository<Candidato, Long> {
    List<Candidato> findByEleccionId(Long eleccionId);
}
