package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, Long> {
    boolean existsByVotanteIdAndEleccionId(Long votanteId, Long eleccionId);

    @Query(value = "SELECT c.id, c.nombre, c.descripcion, c.imagen_url, COUNT(v.id) AS votos " +
            "FROM candidatos c " +
            "LEFT JOIN voto v ON v.candidato_id = c.id AND v.eleccion_id = :eleccionId " +
            "WHERE c.eleccion_id = :eleccionId " +
            "GROUP BY c.id, c.nombre, c.descripcion, c.imagen_url " +
            "ORDER BY votos DESC", nativeQuery = true)
    List<Object[]> contarVotosPorCandidatoEnEleccion(@Param("eleccionId") Long eleccionId);

    @Query(value = "SELECT COUNT(v.id) FROM voto v WHERE v.candidato_id = 0 AND v.eleccion_id = :eleccionId", nativeQuery = true)
    Long contarVotosEnBlanco(@Param("eleccionId") Long eleccionId);
}

