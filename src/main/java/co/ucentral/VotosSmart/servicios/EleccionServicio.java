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
        if (eleccion.getId() != null) {
            // Edición de una elección existente
            Eleccion eleccionExistente = obtenerPorId(eleccion.getId());
            if (eleccionExistente != null && eleccionExistente.getFechaInicio().before(new Date())) {
                throw new IllegalStateException("No se puede editar la elección porque ya ha comenzado.");
            }
            // Actualización de la elección existente
            eleccionExistente.setNombre(eleccion.getNombre());
            eleccionExistente.setDescripcion(eleccion.getDescripcion());
            eleccionExistente.setFechaInicio(eleccion.getFechaInicio());
            eleccionExistente.setFechaFin(eleccion.getFechaFin());
            eleccionExistente.setMaxCandidatos(eleccion.getMaxCandidatos());
            return eleccionRepositorio.save(eleccionExistente);
        } else {
            // Creación de una nueva elección
            return eleccionRepositorio.save(eleccion);
        }
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