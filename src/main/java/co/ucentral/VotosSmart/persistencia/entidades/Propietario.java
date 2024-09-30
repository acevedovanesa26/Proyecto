package co.ucentral.VotosSmart.persistencia.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name =  "Propietario")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Propietario {
    @Id
    @Column (name= "pro_codigo")
    public long codigo;
    @Column (name= "pro_cedula")
    public String placa;
    @Column (name= "pro_nombre")
    public int nombre;
}

