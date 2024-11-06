package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Administrador;
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


@AllArgsConstructor
@Controller
@RequestMapping("/")
public class EleccionControlador {
    private final VotanteServicio votanteServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;

    @GetMapping("añadirEleccion")
    public String mostrarAñadirEleccion(Model model) {
        model.addAttribute("eleccion", new Votante());
        return "añadir-eleccion";
    }
      }




