package co.ucentral.VotosSmart.persistencia.repositorios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EleccionRepositorio extends JpaRepository<Eleccion, Long> {

    List<Eleccion> findAllByFechaFinBefore(Date fecha);

    @Query("SELECT e FROM Eleccion e WHERE (e.fechaInicio <= :ahora AND e.fechaFin >= :ahora) OR e.fechaFin < :ahora")
    List<Eleccion> obtenerEleccionesEnCursoYFinalizadas(@Param("ahora") LocalDateTime ahora);

}

