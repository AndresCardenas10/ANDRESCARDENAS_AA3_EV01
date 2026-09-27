package com.sgb.modelo;

/**
 * Estados posibles de una cita.
 *
 * OJO: los nombres de estas constantes estan en minuscula (pendiente,
 * confirmada, cancelada, completada) en vez de la convencion normal de
 * Java para enums (MAYUSCULAS). Esto es intencional: la columna `estado`
 * de la tabla `cita` en la base de datos real es un ENUM de MySQL con
 * esos mismos valores en minuscula. Como se mapea con
 * @Enumerated(EnumType.STRING) en la entidad Cita, el nombre de la
 * constante de Java tiene que coincidir letra por letra con el valor
 * guardado en MySQL, o Hibernate lanzaria un error al leer o guardar una
 * cita.
 */
public enum EstadoCita {
    pendiente,
    confirmada,
    cancelada,
    completada
}
