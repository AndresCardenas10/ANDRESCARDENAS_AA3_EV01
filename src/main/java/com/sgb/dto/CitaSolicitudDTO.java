package com.sgb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO ("Data Transfer Object") = un objeto simple que solo transporta
 * datos, sin logica. Este es el que se recibe en el "body" de la
 * peticion HTTP cuando alguien crea o actualiza una cita desde la API
 * (por ejemplo, desde Postman o desde el front-end).
 *
 * Se usa un DTO en vez de recibir directamente la entidad Cita porque
 * quien llama a la API no manda objetos Usuario/Servicio completos, sino
 * solo los IDs (numeros) de cliente, barbero y servicio. El servicio
 * (CitaService) es quien busca esos IDs en la base de datos y arma la
 * entidad Cita real.
 */
@Data
public class CitaSolicitudDTO {

    @NotNull(message = "idCliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "idBarbero es obligatorio")
    private Integer idBarbero;

    @NotNull(message = "idServicio es obligatorio")
    private Integer idServicio;

    @NotNull(message = "fechaHora es obligatoria (formato: 2026-10-05T14:30:00)")
    private LocalDateTime fechaHora;

    /** Opcional: comentario libre sobre la cita. */
    private String notas;
}
