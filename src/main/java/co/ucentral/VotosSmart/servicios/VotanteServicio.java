package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Votante;
import co.ucentral.VotosSmart.persistencia.repositorios.VotanteRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class VotanteServicio {

    private final VotanteRepositorio votanteRepositorio;

    public Votante registrarVotante(Votante votante) {
        votante.setCodigoAleatorio(generarCodigoAleatorio());
        return votanteRepositorio.save(votante);
    }
    public Votante obtenerPorCodigoAleatorio(String codigoAleatorio) {
        return votanteRepositorio.findByCodigoAleatorio(codigoAleatorio);
    }

    public List<Votante> obtenerTodos() {
        return (List<Votante>) votanteRepositorio.findAll();
    }

    private String generarCodigoAleatorio() {
        return UUID.randomUUID().toString().substring(0, 8); // Genera un código de 8 caracteres
    }
}
