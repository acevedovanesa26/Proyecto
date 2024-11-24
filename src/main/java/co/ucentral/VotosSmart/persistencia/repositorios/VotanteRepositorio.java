package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Votante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotanteRepositorio extends JpaRepository<Votante, Long> {
    Votante findByCodigoAleatorio(String codigoAleatorio);
}
