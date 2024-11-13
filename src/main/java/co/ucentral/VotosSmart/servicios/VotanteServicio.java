package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Votante;
import co.ucentral.VotosSmart.persistencia.repositorios.VotanteRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class VotanteServicio {

    private final VotanteRepositorio votanteRepositorio;


    public Votante registrarVotante(Votante votante) {
        votante.setCodigoAleatorio(generarCodigoAleatorio());
        votante.setHaVotado(false);
        return votanteRepositorio.save(votante);
    }


    public Votante obtenerPorCodigoAleatorio(String codigoAleatorio) {
        return votanteRepositorio.findByCodigoAleatorio(codigoAleatorio);
    }

    private String generarCodigoAleatorio() {
        return String.valueOf((int)(Math.random() * 100000));
    }



    }




