package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Administrador;
import co.ucentral.VotosSmart.persistencia.repositorios.AdministradorRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdministradorServicio {

    private final AdministradorRepositorio administradorRepositorio;

    public boolean validarCredenciales(String username, String password) {
        Administrador admin = administradorRepositorio.findById(username).orElse(null);
        return admin != null && admin.getPassword().equals(password);
    }

    public void cambiarContraseña(String username, String nuevaContraseña) {
        Administrador administrador = administradorRepositorio.findById(username).orElse(null);
        if (administrador != null) {
            administrador.setPassword(nuevaContraseña);
            administradorRepositorio.save(administrador);
        }
    }

    public void inicializarAdministrador() {
        if (administradorRepositorio.count() == 0) {
            Administrador admin = new Administrador("admin", "123456");
            administradorRepositorio.save(admin);
        }
    }
}
