package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/eleccion")
public class EleccionControlador {

    private final EleccionServicio eleccionServicio;

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaEleccion(Model model) {
        model.addAttribute("eleccion", new Eleccion());
        return "registroEleccion";
    }

    @PostMapping("/guardar")
    public String guardarEleccion(@ModelAttribute Eleccion eleccion, Model model) {
        try {
            // Validaciones
            if (eleccion.getFechaInicio() == null || eleccion.getFechaFin() == null) {
                model.addAttribute("error", "Las fechas de inicio y fin son obligatorias.");
                return "registroEleccion";
            }

            if (eleccion.getMaxCandidatos() > 5) {
                model.addAttribute("error", "El máximo de candidatos no puede superar los cinco.");
                return "registroEleccion";
            }

            if (eleccion.getFechaFin().before(eleccion.getFechaInicio())) {
                model.addAttribute("error", "La fecha de fin no puede ser anterior a la fecha de inicio.");
                return "registroEleccion";
            }

            // Guarda la elección en la base de datos
            eleccionServicio.guardar(eleccion);
            return "redirect:/eleccion/listar";

        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la elección. " + e.getMessage());
            return "registroEleccion";
        }
    }
    @GetMapping("/listar")
    public String listarElecciones(Model model) {
        List<Eleccion> elecciones = eleccionServicio.obtenerTodas();

        for (Eleccion eleccion : elecciones) {
            Date now = new Date();
            if (now.before(eleccion.getFechaInicio())) {
                eleccion.setEstado("Pendiente");
            } else if (now.after(eleccion.getFechaFin())) {
                eleccion.setEstado("Finalizada");
            } else {
                eleccion.setEstado("En Curso");
            }
        }

        model.addAttribute("elecciones", elecciones);
        return "listarElecciones";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarEleccion(@PathVariable Long id, Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(id);
        if (eleccion == null) {
            return "redirect:/eleccion/listar";
        }

        // Verifica si la elección ya ha comenzado
        if (eleccion.getFechaInicio() != null && eleccion.getFechaInicio().before(new Date())) {
            model.addAttribute("error", "No se puede editar la elección porque ya ha comenzado o finalizado.");
            return "redirect:/eleccion/listar";  // Redirige al listado, mostrando el error
        }

        model.addAttribute("eleccion", eleccion);
        return "editarEleccion";
    }


    @PostMapping("/actualizar")
    public String actualizarEleccion(@ModelAttribute Eleccion eleccion, Model model) {
        try {
            eleccionServicio.guardar(eleccion);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eleccion", eleccion);
            return "editarEleccion";
        }
        return "redirect:/eleccion/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEleccion(@PathVariable Long id) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(id);
        if (eleccion != null) {
            eleccionServicio.borrar(eleccion);
        }
        return "redirect:/eleccion/listar";
    }
}
