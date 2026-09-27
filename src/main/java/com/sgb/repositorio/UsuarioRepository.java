package com.sgb.repositorio;

import com.sgb.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso de solo lectura a la tabla `usuario`.
 *
 * JpaRepository ya trae listos metodos como findById(), findAll(),
 * existsById(), etc. No se agrega nada mas aqui porque el modulo de
 * Usuario (login, registro) no es parte de esta evidencia: esta interfaz
 * solo existe para que CitaService pueda verificar que un cliente/barbero
 * exista antes de crear una cita.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
