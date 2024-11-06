package co.ucentral.VotosSmart.controladores;


import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.entidades.Votante;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import co.ucentral.VotosSmart.servicios.VotanteServicio;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@Controller
@RequestMapping("/")
public class EleccionControlador {
    private final VotanteServicio votanteServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;


    // RQ-02: Crear Nueva Elección
    @GetMapping("elecciones")
    public String listarElecciones(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            List<Eleccion> elecciones = eleccionServicio.obtenerTodas();
            model.addAttribute("elecciones", elecciones);
            return "añadir-elecciones";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("eleccion/nueva")
    public String mostrarFormularioNuevaEleccion(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("eleccion", new Eleccion());
            return "añadir-elecciones";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("eleccion/guardar")
    public String guardarEleccion(@ModelAttribute Eleccion eleccion, Model model) {
        if (eleccion.getMaxCandidatos() > 5) {
            model.addAttribute("error", "El máximo de candidatos no puede superar los cinco");
            return "añadir-elecciones";
        }
        eleccionServicio.guardar(eleccion);
        return "redirect:/añadir-elecciones";
    }

      }




