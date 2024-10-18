package co.ucentral.VotosSmart.persistencia.repositorios;


import co.ucentral.VotosSmart.persistencia.entidades.Propietario;
import co.ucentral.VotosSmart.persistencia.entidades.Vehiculo;
import org.springframework.data.repository.CrudRepository;

public interface PropietarioRepositorio extends CrudRepository<Propietario, Long> {
}


