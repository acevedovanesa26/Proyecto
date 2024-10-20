package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import org.springframework.data.repository.CrudRepository;

public interface EleccionRepositorio extends CrudRepository<Eleccion, Long> {
}

