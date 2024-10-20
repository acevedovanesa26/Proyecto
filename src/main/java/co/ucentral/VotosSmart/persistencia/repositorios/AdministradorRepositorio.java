package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Administrador;
import org.springframework.data.repository.CrudRepository;

public interface AdministradorRepositorio extends CrudRepository<Administrador, String> {
}

