package com.sgb.repositorio;

import com.sgb.modelo.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla `servicio`.
 *
 * Spring Data JPA genera automaticamente la consulta SQL de
 * findByActivoTrue() a partir del nombre del metodo (busca todas las
 * filas donde la columna `activo` sea true): no hace falta escribir
 * ningun SQL a mano para esto.
 */
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    List<Servicio> findByActivoTrue();
}
