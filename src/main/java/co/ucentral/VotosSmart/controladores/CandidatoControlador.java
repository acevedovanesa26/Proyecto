package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/candidato")
@AllArgsConstructor
public class CandidatoControlador {

    private final CandidatoServicio candidatoServicio;
    private final EleccionServicio eleccionServicio;

    @GetMapping("/gestionar")
    public String mostrarFormularioGestionarCandidato(Model model) {
        List<Eleccion> eleccionesPendientes = eleccionServicio.obtenerEleccionesPendientes();
        model.addAttribute("candidato", new Candidato());
        model.addAttribute("elecciones", eleccionesPendientes);
        return "gestionarCandidatos";
    }

    @PostMapping("/guardar")
    public String guardarCandidato(@ModelAttribute Candidato candidato,
                                   @RequestParam("eleccionId") Long eleccionId,
                                   @RequestParam("imagen") MultipartFile imagen,
                                   Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(eleccionId);
        if (eleccion == null) {
            model.addAttribute("error", "Elección no válida o no disponible.");
            return "gestionarCandidatos";
        }

        // Validar que no se exceda el número máximo de candidatos permitidos
        List<Candidato> candidatosExistentes = candidatoServicio.obtenerCandidatosPorEleccion(eleccionId);
        if (candidatosExistentes.size() >= eleccion.getMaxCandidatos()) {
            model.addAttribute("error", "Límite de candidatos alcanzado. No puedes agregar más candidatos a esta elección.");
            model.addAttribute("elecciones", eleccionServicio.obtenerEleccionesPendientes()); // Volver a cargar elecciones
            return "gestionarCandidatos";
        }

        candidato.setEleccion(eleccion);
        candidatoServicio.guardarCandidato(candidato, imagen);
        return "redirect:/candidato/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCandidato(@PathVariable Long id, Model model) {
        Candidato candidato = candidatoServicio.obtenerPorId(id).orElse(null);
        if (candidato == null) {
            model.addAttribute("error", "Candidato no encontrado.");
            return "redirect:/candidato/listar";
        }
        List<Eleccion> eleccionesPendientes = eleccionServicio.obtenerEleccionesPendientes();
        model.addAttribute("candidato", candidato);
        model.addAttribute("elecciones", eleccionesPendientes);
        return "editarCandidato"; // Cambiado para redirigir a una vista específica de edición
    }

    @PostMapping("/actualizar")
    public String actualizarCandidato(@ModelAttribute Candidato candidato,
                                      @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                      Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(candidato.getEleccion().getId());
        if (eleccion == null) {
            model.addAttribute("error", "Elección no válida o no disponible.");
            model.addAttribute("candidato", candidato);
            model.addAttribute("elecciones", eleccionServicio.obtenerEleccionesPendientes());
            return "editarCandidato"; // Devuelve la vista de edición con el mensaje de error
        }

        candidato.setEleccion(eleccion);

        if (imagen != null && !imagen.isEmpty()) {
            candidatoServicio.eliminarImagenFisica(candidato.getImagenUrl()); // Eliminar imagen anterior
            candidatoServicio.guardarCandidato(candidato, imagen); // Guardar nueva imagen
        } else {
            candidatoServicio.guardarCandidato(candidato, null); // Actualizar sin cambiar la imagen
        }

        return "redirect:/candidato/listar"; // Redirigir al listado de candidatos después de actualizar
    }

    @GetMapping("/listar")
    public String listarCandidatos(Model model) {
        List<Candidato> candidatos = candidatoServicio.obtenerTodos();
        model.addAttribute("candidatos", candidatos);
        return "listarCandidatos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCandidato(@PathVariable Long id) {
        candidatoServicio.eliminarCandidato(id);
        return "redirect:/candidato/listar";
    }
}
