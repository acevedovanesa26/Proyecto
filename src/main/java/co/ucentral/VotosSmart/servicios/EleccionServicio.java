package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.repositorios.EleccionRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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



    public void borrar(Eleccion eleccion) {
        eleccionRepositorio.delete(eleccion);
    }

    public Eleccion obtenerPorId(Long id) {
        return eleccionRepositorio.findById(id).orElse(null);
    }

    public List<Eleccion> obtenerEleccionesPendientes() {
        return eleccionRepositorio.findAll().stream()
                .filter(e -> e.getFechaInicio().after(new Date()))
                .collect(Collectors.toList());
    }


    public List<Eleccion> obtenerEleccionesEnCurso() {
        Date fechaActual = new Date();
        return eleccionRepositorio.findAll().stream()
                .filter(e -> e.getFechaInicio().before(fechaActual) && e.getFechaFin().after(fechaActual))
                .collect(Collectors.toList());
    }

    public List<Eleccion> obtenerEleccionesEnCursoYFinalizadas() {
        LocalDateTime ahora = LocalDateTime.now();
        return eleccionRepositorio.obtenerEleccionesEnCursoYFinalizadas(ahora);
    }

    public Eleccion obtenerEleccionPorId(Long eleccionId) {
        return eleccionRepositorio.findById(eleccionId).orElse(null);
    }

    public List<Eleccion> obtenerEleccionesFinalizadas() {
        Date fechaActual = new Date();
        return eleccionRepositorio.findAllByFechaFinBefore(fechaActual);
    }


}






