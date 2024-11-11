package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
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
public class VotanteControlador {

    private final VotanteServicio votanteServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;

    @GetMapping("/votante/registro")
    public String mostrarFormularioRegistroVotante(Model model) {
        model.addAttribute("votante", new Votante());
        return "registroVotante";
    }
    // RQ-04 y RQ-05: Gestión de Votantes
    @GetMapping("votantes")
    public String listarVotantes(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            List<Votante> votantes = votanteServicio.obtenerTodos();
            model.addAttribute("votantes", votantes);
            return "votantes";
        } else {
            return "redirect:/login";
        }
    }

    // Otros métodos

    // Métodos de gestión de votantes

    @GetMapping("registroVotante")
    public String mostrarRegistroVotante(Model model) {
        model.addAttribute("votante", new Votante());
        return "registroVotante";
    }

    @PostMapping("/votante/login")
    public String iniciarSesionVotante(@RequestParam("codigoAleatorio") String codigoAleatorio, Model model, HttpSession session) {
        Votante votante = votanteServicio.obtenerPorCodigoAleatorio(codigoAleatorio);
        if (votante != null) {
            session.setAttribute("votanteId", votante.getId());
            return "redirect:/votacion"; // Cambia a la vista o página correcta
        } else {
            model.addAttribute("error", "Código aleatorio no válido.");
            return "loginVotante";
        }
    }


    @GetMapping("loginVotante")
    public String mostrarLoginVotante(Model model) {
        model.addAttribute("votante", new Votante());
        return "loginVotante";
    }

    @PostMapping("loginVotante")
    public String procesarLoginVotante(@RequestParam String codigoAleatorio, Model model, HttpSession session) {
        Votante votante = votanteServicio.obtenerPorCodigoAleatorio(codigoAleatorio);
        if (votante != null) {
            session.setAttribute("votanteCodigo", votante.getCodigoAleatorio());
            return "redirect:/elecciones";
        } else {
            model.addAttribute("error", "Código aleatorio inválido.");
            return "loginVotante";
        }
    }




    }


