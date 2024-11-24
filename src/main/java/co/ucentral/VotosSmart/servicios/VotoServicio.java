package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import co.ucentral.VotosSmart.persistencia.repositorios.CandidatoRepositorio;
import co.ucentral.VotosSmart.persistencia.repositorios.VotoRepositorio;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.awt.*;

import java.io.IOException;
import java.util.List;




@Service
@AllArgsConstructor
public class VotoServicio {

    private final VotoRepositorio votoRepositorio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoRepositorio candidatoRepositorio;

    public void registrarVoto(Long votanteId, Long candidatoId, Long eleccionId) {
        Voto voto = new Voto();
        voto.setVotanteId(votanteId);
        voto.setCandidatoId(candidatoId);
        voto.setEleccionId(eleccionId);
        votoRepositorio.save(voto);
    }

    // Métodopara verificar si el votante ya votó en una elección específica
    public boolean haVotadoEnEleccion(Long votanteId, Long eleccionId) {
        return votoRepositorio.existsByVotanteIdAndEleccionId(votanteId, eleccionId);
    }
    public List<Object[]> obtenerResultadosPorEleccion(Long eleccionId) {
        return votoRepositorio.contarVotosPorCandidatoEnEleccion(eleccionId);
    }

    public Long obtenerVotosEnBlanco(Long eleccionId) {
        return votoRepositorio.contarVotosEnBlanco(eleccionId);
    }


}
