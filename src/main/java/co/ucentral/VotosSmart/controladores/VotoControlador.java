package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import co.ucentral.VotosSmart.servicios.VotoServicio;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/votante")
public class VotoControlador {

    private final VotoServicio votoServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;

    @GetMapping("/votar/{eleccionId}")
    public String mostrarCandidatosParaVotar(@PathVariable Long eleccionId, HttpSession session, Model model) {
        session.setAttribute("eleccionId", eleccionId); // Guardar elección en sesión
        List<Candidato> candidatos = candidatoServicio.obtenerCandidatosPorEleccion(eleccionId);
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("eleccionId", eleccionId);
        return "candidatosParaVotar"; // Vista con candidatos de la elección
    }

    @GetMapping("/votarPorCandidato/{candidatoId}")
    public String votarPorCandidato(@PathVariable Long candidatoId, HttpSession session, Model model) {
        Long votanteId = (Long) session.getAttribute("votanteId");
        Long eleccionId = (Long) session.getAttribute("eleccionId");

        if (votanteId == null || eleccionId == null) {
            model.addAttribute("error", "Error al registrar el voto. Intenta de nuevo.");
            return "candidatosParaVotar";
        }

        // Verifica si el votante ya ha votado en esta eleccion ds
        if (votoServicio.haVotadoEnEleccion(votanteId, eleccionId)) {
            model.addAttribute("error", "Ya has votado en esta elección.");
            return "candidatosParaVotar"; // Vuelve a mostrar los candidatos con el mensaje de error
        }

        // Registrar el voto si no ha votado // listooooooo
        votoServicio.registrarVoto(votanteId, candidatoId, eleccionId);
        return "redirect:/votante/confirmacion";
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacionVoto() {
        return "confirmacionVoto";
    }
}