package co.ucentral.VotosSmart.persistencia.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;

@Entity
@Table (name =  "Vehiculos")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehiculo {
    @Id
    @Column (name= "veh_codigo")
    public long codigo;
    @Column (name= "veh_placa")
    public String placa;
    @Column (name= "veh_modelo")
    public int modelo;
}