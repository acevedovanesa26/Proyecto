package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.*;
import co.ucentral.VotosSmart.servicios.AdministradorServicio;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import co.ucentral.VotosSmart.servicios.PDFServicio;
import co.ucentral.VotosSmart.servicios.VotanteServicio;
import co.ucentral.VotosSmart.servicios.VotoServicio;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class AdministradorControlador {

    private final AdministradorServicio administradorServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;
    private final VotoServicio votoServicio;
    private final PDFServicio pdfServicio; // Servicio para generar PDFs


    @GetMapping("/")
    public String mostrarSeleccionUsuario() {
        return "index"; // Retorna index.html en la carpeta templates
    }


    @GetMapping("/inicio")
    public String redireccionarInicio(HttpSession session) {
        if (session.getAttribute("adminUsername") != null) {
            return "redirect:/login";
        } else {
            return "redirect:/index";
        }
    }

    // RQ-01: Inicio de Sesion del Administrador
    @GetMapping("login")
    public String mostrarLogin(Model model) {
        model.addAttribute("administrador", new Administrador());
        return "login";
    }

    @PostMapping("login")
    public String procesarLogin(@ModelAttribute Administrador administrador, Model model, HttpSession session) {
        if (administradorServicio.validarCredenciales(administrador.getUsername(), administrador.getPassword())) {
            session.setAttribute("adminUsername", administrador.getUsername());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Credenciales inválidas. Inténtalo de nuevo.");
            return "login";
        }
    }

    @GetMapping("logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // RQ-01: Cambiar Contraseña
    @GetMapping("cambiar-contraseña")
    public String mostrarCambioContraseña(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("administrador", new Administrador());
            return "cambiar-contraseña";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("cambiar-contraseña")
    public String cambiarContraseña(@RequestParam("nuevaContraseña") String nuevaContraseña,
                                    @RequestParam("confirmarContraseña") String confirmarContraseña,
                                    HttpSession session,
                                    Model model) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }

        if (!nuevaContraseña.equals(confirmarContraseña)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "cambiar-contraseña";
        }

        String username = (String) session.getAttribute("adminUsername");
        administradorServicio.cambiarContraseña(username, nuevaContraseña);
        model.addAttribute("mensaje", "Contraseña actualizada exitosamente");
        return "cambiar-contraseña";
    }

    // Dashboard
    @GetMapping("dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("username", session.getAttribute("adminUsername"));
            return "dashboard";
        } else {
            return "redirect:/login";
        }
    }




}
