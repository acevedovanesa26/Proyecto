package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.servicios.VotoServicio;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/votante")
public class VotoControlador {

    private final VotoServicio votoServicio;

    @GetMapping("/votarPorCandidato/{candidatoId}")
    public String votarPorCandidato(@PathVariable Long candidatoId, HttpSession session, Model model) {
        Long votanteId = (Long) session.getAttribute("votanteId");
        Long eleccionId = (Long) session.getAttribute("eleccionId");

        if (votanteId != null && eleccionId != null) {
            boolean votoRegistrado = votoServicio.registrarVoto(votanteId, candidatoId, eleccionId);
            if (votoRegistrado) {
                return "redirect:confirmacionVoto"; // Redirige a la página de confirmación si se registra correctamente
            } else {
                model.addAttribute("error", "Ya has votado en esta elección.");
                return "candidatosParaVotar";
            }
        } else {
            model.addAttribute("error", "Error al registrar el voto. Intenta de nuevo.");
            return "candidatosParaVotar";
        }
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacionVoto() {
        return "confirmacionVoto"; // Redirige a una vista de confirmación
    }
}
