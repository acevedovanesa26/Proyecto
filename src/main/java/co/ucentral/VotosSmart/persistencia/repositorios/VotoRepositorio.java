package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, Long> {

    // Verificar si el votante ya ha votado en una elección específica
    boolean existsByVotanteIdAndEleccionId(Long votanteId, Long eleccionId);
}
