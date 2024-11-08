package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class CandidatoControlador {

    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;

    // Listar candidatos de una elección
    @GetMapping("eleccion/{id}/candidatos")
    public String listarCandidatos(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return "redirect:/listarElecciones";  // Asegúrate de que listarElecciones es el nombre correcto de la vista
            }
            List<Candidato> candidatos = candidatoServicio.obtenerPorEleccionId(id);
            model.addAttribute("candidatos", candidatos);
            model.addAttribute("eleccion", eleccion);
            return "candidatos";
        } else {
            return "redirect:/login";
        }
    }

    // Mostrar formulario para agregar un nuevo candidato
    @GetMapping("eleccion/{id}/candidato/nuevo")
    public String mostrarFormularioNuevoCandidato(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return "redirect:/listarElecciones";
            }
            model.addAttribute("candidato", new Candidato());
            model.addAttribute("eleccion", eleccion);
            return "nuevoCandidato";
        } else {
            return "redirect:/login";
        }
    }

    // Guardar un nuevo candidato
    @PostMapping("eleccion/{id}/candidato/guardar")
    public String guardarCandidato(@PathVariable Long id,
                                   @ModelAttribute Candidato candidato,
                                   @RequestParam("imagen") MultipartFile imagen,
                                   Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(id);
        if (eleccion == null) {
            return "redirect:/listarElecciones";
        }
        List<Candidato> candidatosExistentes = candidatoServicio.obtenerPorEleccionId(id);
        if (candidatosExistentes.size() >= eleccion.getMaxCandidatos()) {
            model.addAttribute("error", "Se ha alcanzado el número máximo de candidatos para esta elección");
            return "redirect:/eleccion/" + id + "/candidatos";
        }

        if (!imagen.isEmpty()) {
            try {
                String imagenUrl = guardarImagen(imagen);
                candidato.setImagenUrl(imagenUrl);
            } catch (Exception e) {
                model.addAttribute("error", "Error al guardar la imagen");
                return "nuevoCandidato";
            }
        }
        candidato.setEleccion(eleccion);
        candidatoServicio.guardar(candidato);
        return "redirect:/eleccion/" + id + "/candidatos";
    }

    // Método para guardar la imagen del candidato
    private String guardarImagen(MultipartFile imagen) throws Exception {
        String imagenUrl = "/imagenes/" + imagen.getOriginalFilename();
        return imagenUrl;
    }

    // Editar un candidato
    @GetMapping("eleccion/{idEleccion}/candidato/editar/{idCandidato}")
    public String mostrarFormularioEditarCandidato(@PathVariable Long idEleccion, @PathVariable Long idCandidato, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
            Candidato candidato = candidatoServicio.obtenerPorId(idCandidato);
            if (eleccion == null || candidato == null) {
                return "redirect:/listarElecciones";
            }
            model.addAttribute("candidato", candidato);
            model.addAttribute("eleccion", eleccion);
            return "editarCandidato";
        } else {
            return "redirect:/login";
        }
    }

    // Actualizar un candidato
    @PostMapping("eleccion/{idEleccion}/candidato/actualizar")
    public String actualizarCandidato(@PathVariable Long idEleccion,
                                      @ModelAttribute Candidato candidato,
                                      @RequestParam("imagen") MultipartFile imagen,
                                      Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
        if (eleccion == null) {
            return "redirect:/listarElecciones";
        }

        if (!imagen.isEmpty()) {
            try {
                String imagenUrl = guardarImagen(imagen);
                candidato.setImagenUrl(imagenUrl);
            } catch (Exception e) {
                model.addAttribute("error", "Error al actualizar la imagen");
                return "editarCandidato";
            }
        }
        candidato.setEleccion(eleccion);
        candidatoServicio.guardar(candidato);
        return "redirect:/eleccion/" + idEleccion + "/candidatos";
    }

    // Eliminar un candidato
    @GetMapping("eleccion/{idEleccion}/candidato/eliminar/{idCandidato}")
    public String eliminarCandidato(@PathVariable Long idEleccion, @PathVariable Long idCandidato, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
            Candidato candidato = candidatoServicio.obtenerPorId(idCandidato);
            if (eleccion == null || candidato == null) {
                return "redirect:/listarElecciones";
            }
            candidatoServicio.borrar(candidato);
            return "redirect:/eleccion/" + idEleccion + "/candidatos";
        } else {
            return "redirect:/login";
        }
    }
}
