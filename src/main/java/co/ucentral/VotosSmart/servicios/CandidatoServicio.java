package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.repositorios.CandidatoRepositorio;
import co.ucentral.VotosSmart.persistencia.repositorios.EleccionRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidatoServicio {

    private final CandidatoRepositorio candidatoRepositorio;
    private final EleccionRepositorio eleccionRepositorio;
    private static final String IMAGENES_DIR = "src/main/resources/static/imagenes/";

    public void guardarCandidato(Candidato candidato, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = guardarImagen(imagen);
            candidato.setImagenUrl(rutaImagen);
        }
        candidatoRepositorio.save(candidato);
    }

    private String guardarImagen(MultipartFile imagen) {
        try {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(IMAGENES_DIR, nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo);
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Candidato> obtenerPorId(Long id) {
        return candidatoRepositorio.findById(id);
    }

    public List<Candidato> obtenerTodos() {
        return (List<Candidato>) candidatoRepositorio.findAll();
    }

    public void eliminarCandidato(Long id) {
        Candidato candidato = candidatoRepositorio.findById(id).orElse(null);
        if (candidato != null) {
            eliminarImagenFisica(candidato.getImagenUrl());
            candidatoRepositorio.delete(candidato);
        }
    }

    private void eliminarImagenFisica(String imagenUrl) {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            try {
                Path rutaImagen = Paths.get(IMAGENES_DIR, imagenUrl);
                Files.deleteIfExists(rutaImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Eleccion> obtenerEleccionesPendientes() {
        return eleccionRepositorio.findAll().stream()
                .filter(e -> e.getFechaInicio().after(new Date()))
                .collect(Collectors.toList());
    }

    public List<Candidato> obtenerCandidatosPorEleccion(Long eleccionId) {
        return candidatoRepositorio.findByEleccionId(eleccionId);
    }
}
