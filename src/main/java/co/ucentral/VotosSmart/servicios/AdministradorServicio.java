package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Administrador;
import co.ucentral.VotosSmart.persistencia.repositorios.AdministradorRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdministradorServicio {

    private final AdministradorRepositorio administradorRepositorio;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean validarCredenciales(String username, String password) {
        Administrador admin = administradorRepositorio.findById(username).orElse(null);
        return admin != null && passwordEncoder.matches(password, admin.getPassword());
    }

    public void cambiarContraseña(String username, String nuevaContraseña) {
        Administrador administrador = administradorRepositorio.findById(username).orElse(null);
        if (administrador != null) {
            administrador.setPassword(passwordEncoder.encode(nuevaContraseña));
            administradorRepositorio.save(administrador);
        }
    }

    public void inicializarAdministrador() {
        if (administradorRepositorio.count() == 0) {
            String adminUsername = System.getProperty("ADMIN_USERNAME");
            String adminPassword = System.getProperty("ADMIN_PASSWORD");
            String encodedPassword = passwordEncoder.encode(adminPassword);
            Administrador admin = new Administrador(adminUsername, encodedPassword);
            administradorRepositorio.save(admin);
        }
    }
}
