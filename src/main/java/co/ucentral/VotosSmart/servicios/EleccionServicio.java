package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.repositorios.EleccionRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EleccionServicio {

    private final EleccionRepositorio eleccionRepositorio;

    public List<Eleccion> obtenerTodas() {
        return (List<Eleccion>) eleccionRepositorio.findAll();
    }

    public Eleccion guardar(Eleccion eleccion) {
        return eleccionRepositorio.save(eleccion);
    }

    public Eleccion obtenerPorId(Long id) {
        return eleccionRepositorio.findById(id).orElse(null);
    }

    public boolean borrar(Eleccion eleccion) {
        try {
            eleccionRepositorio.delete(eleccion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
