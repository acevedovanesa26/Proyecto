package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.*;
import co.ucentral.VotosSmart.servicios.AdministradorServicio;
import co.ucentral.VotosSmart.servicios.CandidatoServicio;
import co.ucentral.VotosSmart.servicios.EleccionServicio;
import co.ucentral.VotosSmart.servicios.VotoServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class AdministradorControlador {

    private final AdministradorServicio administradorServicio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoServicio candidatoServicio;
    private final VotoServicio votoServicio;



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
    @GetMapping("/resultados")
    public String verResultadosTiempoReal(Model model) {
        // Obtener elecciones en curso y finalizadas
        List<Eleccion> elecciones = eleccionServicio.obtenerEleccionesEnCursoYFinalizadas();

        // Mapas para almacenar los resultados y votos en blanco por elección
        Map<Long, List<Object[]>> resultadosPorEleccion = new HashMap<>();
        Map<Long, Long> votosEnBlancoPorEleccion = new HashMap<>();

        for (Eleccion eleccion : elecciones) {
            List<Object[]> resultados = votoServicio.obtenerResultadosPorEleccion(eleccion.getId());
            Long votosEnBlanco = votoServicio.obtenerVotosEnBlanco(eleccion.getId());

            resultadosPorEleccion.put(eleccion.getId(), resultados);
            votosEnBlancoPorEleccion.put(eleccion.getId(), votosEnBlanco);
        }

        // Agregamos los datos al modelo
        model.addAttribute("elecciones", elecciones);
        model.addAttribute("resultadosPorEleccion", resultadosPorEleccion);
        model.addAttribute("votosEnBlancoPorEleccion", votosEnBlancoPorEleccion);

        return "resultadosEnTiempoReal";
    }


    @GetMapping("/generarPDF")
    public String mostrarEleccionesFinalizadas(Model model) {
        List<Eleccion> eleccionesFinalizadas = eleccionServicio.obtenerEleccionesFinalizadas();
        model.addAttribute("elecciones", eleccionesFinalizadas);
        return "generarPDF"; // Asegúrate de tener generarPDF.html en templates
    }

    @GetMapping("/generarPDF/{eleccionId}")
    public ResponseEntity<byte[]> generarPDF(@PathVariable Long eleccionId) {
        byte[] contenidoPDF = votoServicio.generarReportePDF(eleccionId);

        Eleccion eleccion = eleccionServicio.obtenerEleccionPorId(eleccionId);
        if (eleccion == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String nombreArchivo = eleccion.getNombre().replaceAll("\\s+", "_") + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Configura el encabezado para la descarga del archivo
        headers.setContentDispositionFormData(nombreArchivo, nombreArchivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(contenidoPDF);
    }
}
