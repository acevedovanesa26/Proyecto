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
    private final VotanteServicio votanteServicio;
    private final VotoServicio votoServicio;
    private final PDFServicio pdfServicio; // Servicio para generar PDFs

    @GetMapping("/")
    public String redireccionarInicio(HttpSession session) {
        if (session.getAttribute("adminUsername") != null) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/login";
        }
    }


    // RQ-01: Inicio de Sesión del Administrador
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
        return "redirect:/login";
    }

    // RQ-01: Cambiar Contraseña
    @GetMapping("admin/cambiar-contraseña")
    public String mostrarCambioContraseña(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("administrador", new Administrador());
            return "cambiarContraseña";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("admin/cambiar-contraseña")
    public String cambiarContraseña(@RequestParam("nuevaContraseña") String nuevaContraseña,
                                    @RequestParam("confirmarContraseña") String confirmarContraseña,
                                    HttpSession session,
                                    Model model) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }

        if (!nuevaContraseña.equals(confirmarContraseña)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "cambiarContraseña";
        }

        String username = (String) session.getAttribute("adminUsername");
        administradorServicio.cambiarContraseña(username, nuevaContraseña);
        model.addAttribute("mensaje", "Contraseña actualizada exitosamente");
        return "cambiarContraseña";
    }

    // Dashboard Principal
    @GetMapping("dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("username", session.getAttribute("adminUsername"));
            return "dashboard";
        } else {
            return "redirect:/login";
        }
    }

    // RQ-02: Crear Nueva Elección
    @GetMapping("elecciones")
    public String listarElecciones(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            List<Eleccion> elecciones = eleccionServicio.obtenerTodas();
            model.addAttribute("elecciones", elecciones);
            return "elecciones";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("eleccion/nueva")
    public String mostrarFormularioNuevaEleccion(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            model.addAttribute("eleccion", new Eleccion());
            return "nuevaEleccion";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("eleccion/guardar")
    public String guardarEleccion(@ModelAttribute Eleccion eleccion, Model model) {
        if (eleccion.getMaxCandidatos() > 5) {
            model.addAttribute("error", "El máximo de candidatos no puede superar los cinco");
            return "nuevaEleccion";
        }
        eleccionServicio.guardar(eleccion);
        return "redirect:/elecciones";
    }

    // RQ-03: Gestionar Candidatos
    @GetMapping("eleccion/{id}/candidatos")
    public String listarCandidatos(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return "redirect:/elecciones";
            }
            List<Candidato> candidatos = candidatoServicio.obtenerPorEleccionId(id);
            model.addAttribute("candidatos", candidatos);
            model.addAttribute("eleccion", eleccion);
            return "candidatos";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("eleccion/{id}/candidato/nuevo")
    public String mostrarFormularioNuevoCandidato(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return "redirect:/elecciones";
            }
            if (new Date().after(eleccion.getFechaInicio())) {
                model.addAttribute("error", "No se pueden añadir candidatos después del inicio de la elección");
                return "redirect:/eleccion/" + id + "/candidatos";
            }
            model.addAttribute("candidato", new Candidato());
            model.addAttribute("eleccion", eleccion);
            return "nuevoCandidato";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("eleccion/{id}/candidato/guardar")
    public String guardarCandidato(@PathVariable Long id,
                                   @ModelAttribute Candidato candidato,
                                   @RequestParam("imagen") MultipartFile imagen,
                                   Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(id);
        if (eleccion == null) {
            return "redirect:/elecciones";
        }
        if (new Date().after(eleccion.getFechaInicio())) {
            model.addAttribute("error", "No se pueden añadir candidatos después del inicio de la elección");
            return "redirect:/eleccion/" + id + "/candidatos";
        }

        // Verificar si se ha alcanzado el máximo de candidatos
        List<Candidato> candidatosExistentes = candidatoServicio.obtenerPorEleccionId(id);
        if (candidatosExistentes.size() >= eleccion.getMaxCandidatos()) {
            model.addAttribute("error", "Se ha alcanzado el número máximo de candidatos para esta elección");
            return "redirect:/eleccion/" + id + "/candidatos";
        }

        // Lógica para guardar la imagen (opcional)
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

    // Método para guardar la imagen en el servidor
    private String guardarImagen(MultipartFile imagen) throws Exception {
        // Implementa aquí la lógica para guardar la imagen y devolver la URL o ruta de acceso
        // Por simplicidad, supongamos que la imagen se guarda correctamente y devolvemos una URL ficticia
        String imagenUrl = "/imagenes/" + imagen.getOriginalFilename();
        // Código para guardar la imagen en el directorio correspondiente
        return imagenUrl;
    }

    // RQ-03: Editar y Eliminar Candidatos
    @GetMapping("eleccion/{idEleccion}/candidato/editar/{idCandidato}")
    public String mostrarFormularioEditarCandidato(@PathVariable Long idEleccion, @PathVariable Long idCandidato, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
            Candidato candidato = candidatoServicio.obtenerPorId(idCandidato);
            if (eleccion == null || candidato == null) {
                return "redirect:/elecciones";
            }
            if (new Date().after(eleccion.getFechaInicio())) {
                model.addAttribute("error", "No se pueden editar candidatos después del inicio de la elección");
                return "redirect:/eleccion/" + idEleccion + "/candidatos";
            }
            model.addAttribute("candidato", candidato);
            model.addAttribute("eleccion", eleccion);
            return "editarCandidato";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("eleccion/{idEleccion}/candidato/actualizar")
    public String actualizarCandidato(@PathVariable Long idEleccion,
                                      @ModelAttribute Candidato candidato,
                                      @RequestParam("imagen") MultipartFile imagen,
                                      Model model) {
        Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
        if (eleccion == null) {
            return "redirect:/elecciones";
        }
        if (new Date().after(eleccion.getFechaInicio())) {
            model.addAttribute("error", "No se pueden editar candidatos después del inicio de la elección");
            return "redirect:/eleccion/" + idEleccion + "/candidatos";
        }

        // Lógica para actualizar la imagen si se proporciona una nueva
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

    @GetMapping("eleccion/{idEleccion}/candidato/eliminar/{idCandidato}")
    public String eliminarCandidato(@PathVariable Long idEleccion, @PathVariable Long idCandidato, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(idEleccion);
            Candidato candidato = candidatoServicio.obtenerPorId(idCandidato);
            if (eleccion == null || candidato == null) {
                return "redirect:/elecciones";
            }
            if (new Date().after(eleccion.getFechaInicio())) {
                model.addAttribute("error", "No se pueden eliminar candidatos después del inicio de la elección");
                return "redirect:/eleccion/" + idEleccion + "/candidatos";
            }
            candidatoServicio.borrar(candidato);
            return "redirect:/eleccion/" + idEleccion + "/candidatos";
        } else {
            return "redirect:/login";
        }
    }

    // RQ-06: Visualizar Resultados en Tiempo Real
    @GetMapping("eleccion/{id}/resultados")
    public String verResultados(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return "redirect:/elecciones";
            }
            List<Candidato> candidatos = candidatoServicio.obtenerPorEleccionId(id);
            // Obtener votos por candidato
            for (Candidato candidato : candidatos) {
                Long votosCandidato = votoServicio.contarVotosPorCandidato(candidato.getId());
                candidato.setNumeroVotos(votosCandidato);
            }
            model.addAttribute("eleccion", eleccion);
            model.addAttribute("candidatos", candidatos);
            return "resultados";
        } else {
            return "redirect:/login";
        }
    }

    // RQ-07: Exportar Resultados en PDF
    @GetMapping("eleccion/{id}/exportar")
    public ResponseEntity<InputStreamResource> exportarResultados(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminUsername") != null) {
            Eleccion eleccion = eleccionServicio.obtenerPorId(id);
            if (eleccion == null) {
                return ResponseEntity.badRequest().build();
            }
            ByteArrayInputStream bis = pdfServicio.generarReporteResultados(eleccion);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=resultados_eleccion_" + eleccion.getId() + ".pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } else {
            return ResponseEntity.status(401).build();
        }
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

    // Otros métodos adicionales según sea necesario...
}
