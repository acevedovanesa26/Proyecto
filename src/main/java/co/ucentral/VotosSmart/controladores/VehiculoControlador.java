package co.ucentral.VotosSmart.controladores;

import co.ucentral.VotosSmart.persistencia.entidades.Vehiculo;
import co.ucentral.VotosSmart.servicios.VehiculoServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
public class VehiculoControlador {

    VehiculoServicio vehiculoServicio;

    public List<Vehiculo> obtenerTodos(){
        return vehiculoServicio.obtenerTodos();
    }
}