package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepositorio extends JpaRepository<Voto, Long> {
    // Agrega métodos personalizados si es necesario
}
