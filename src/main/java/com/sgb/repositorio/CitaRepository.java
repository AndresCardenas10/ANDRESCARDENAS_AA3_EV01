package com.sgb.repositorio;

import com.sgb.modelo.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla `cita`.
 *
 * Los nombres de estos metodos siguen la convencion de Spring Data JPA:
 * "findByCliente_IdUsuario" se traduce, sin escribir SQL, a
 * "SELECT * FROM cita WHERE id_cliente = ?". El guion bajo indica que
 * "IdUsuario" es un campo de la entidad relacionada (Usuario), no de Cita.
 */
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByCliente_IdUsuario(Integer idCliente);

    List<Cita> findByBarbero_IdUsuario(Integer idBarbero);
}
