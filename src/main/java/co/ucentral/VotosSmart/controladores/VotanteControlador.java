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
@RequestMapping("/votante")
public class VotanteControlador {

    private final VotanteServicio votanteServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;


    // Inicio de sesión del votante
    @GetMapping("/login")
    public String mostrarLoginVotante() {
        return "loginVotante"; // Nombre de la vista de login para votante
    }


    @GetMapping("/registro")
    public String mostrarFormularioRegistroVotante(Model model) {
        model.addAttribute("votante", new Votante());
        return "registroVotante";
    }

    @PostMapping("/registro")
    public String registrarVotante(@ModelAttribute Votante votante, Model model) {
        try {
            Votante registrado = votanteServicio.registrarVotante(votante);
            model.addAttribute("success", "Registro exitoso. Tu código es: " + registrado.getCodigoAleatorio());
            return "registroVotante";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el votante. Verifica los datos e inténtalo de nuevo.");
            return "registroVotante";
        }
    }

    @PostMapping("/login")
    public String iniciarSesionVotante(@RequestParam("codigoAleatorio") String codigoAleatorio, HttpSession session, Model model) {
        Votante votante = votanteServicio.obtenerPorCodigoAleatorio(codigoAleatorio);
        if (votante != null) {
            session.setAttribute("votanteId", votante.getId());
            return "redirect:/votante/elecciones";
        } else {
            model.addAttribute("error", "Código aleatorio inválido.");
            return "loginVotante";
        }
    }

    @GetMapping("/elecciones")
    public String mostrarEleccionesDisponibles(Model model) {
        List<Eleccion> eleccionesEnCurso = eleccionServicio.obtenerEleccionesEnCurso();
        model.addAttribute("elecciones", eleccionesEnCurso);
        return "eleccionesDisponibles";
    }


}
