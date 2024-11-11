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
        if (eleccion != null) {
            candidato.setEleccion(eleccion);
            candidatoServicio.guardarCandidato(candidato, imagen);
        } else {
            model.addAttribute("error", "Elección no válida o no disponible.");
            return "gestionarCandidatos";
        }
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
        return "gestionarCandidatos";
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
