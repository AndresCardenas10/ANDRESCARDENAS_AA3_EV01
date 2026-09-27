package com.sgb.dto;

import com.sgb.modelo.Cita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que se devuelve al consultar una o varias citas.
 *
 * En vez de devolver la entidad Cita tal cual (que tiene referencias
 * "perezosas" -lazy- a Usuario y Servicio que Hibernate no ha cargado
 * todavia), se copian aqui solo los datos ya "aplanados" y listos para
 * mostrar: el nombre del cliente y del barbero en vez del objeto Usuario
 * completo, el nombre del servicio en vez del objeto Servicio completo.
 *
 * Esto evita un error muy comun al trabajar con JPA (LazyInitializationException)
 * y ademas produce una respuesta JSON mas simple y facil de leer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRespuestaDTO {

    private Integer idCita;
    private Integer idCliente;
    private String cliente;
    private Integer idBarbero;
    private String barbero;
    private Integer idServicio;
    private String servicio;
    private BigDecimal precioServicio;
    private LocalDateTime fechaHora;
    private String estado;
    private LocalDateTime expiraEn;
    private String notas;
    private LocalDateTime creadoEn;

    /**
     * Convierte una entidad Cita (con sus relaciones ya cargadas dentro
     * de la transaccion, en CitaService) a este DTO "plano".
     */
    public static CitaRespuestaDTO desde(Cita cita) {
        return new CitaRespuestaDTO(
                cita.getIdCita(),
                cita.getCliente().getIdUsuario(),
                cita.getCliente().getNombreCompleto(),
                cita.getBarbero().getIdUsuario(),
                cita.getBarbero().getNombreCompleto(),
                cita.getServicio().getIdServicio(),
                cita.getServicio().getNombre(),
                cita.getServicio().getPrecio(),
                cita.getFechaHora(),
                cita.getEstado().name(),
                cita.getExpiraEn(),
                cita.getNotas(),
                cita.getCreadoEn()
        );
    }
}
