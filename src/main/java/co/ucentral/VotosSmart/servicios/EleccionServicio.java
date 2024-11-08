package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.repositorios.EleccionRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EleccionServicio {

    private final EleccionRepositorio eleccionRepositorio;

    public Eleccion guardar(Eleccion eleccion) {
        if (eleccion.getId() != null && eleccion.getFechaInicio().before(new Date())) {
            throw new IllegalStateException("No se puede editar la elección porque ya ha comenzado.");
        }
        return eleccionRepositorio.save(eleccion);
    }

    public List<Eleccion> obtenerTodas() {
        return (List<Eleccion>) eleccionRepositorio.findAll();
    }

    public Eleccion obtenerPorId(Long id) {
        return eleccionRepositorio.findById(id).orElse(null);
    }

    public void borrar(Eleccion eleccion) {
        eleccionRepositorio.delete(eleccion);
    }
}
