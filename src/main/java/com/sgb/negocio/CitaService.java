package com.sgb.negocio;

import com.sgb.modelo.Cita;
import com.sgb.modelo.EstadoCita;
import com.sgb.modelo.Servicio;
import com.sgb.modelo.Usuario;
import com.sgb.repositorio.CitaRepository;
import com.sgb.repositorio.ServicioRepository;
import com.sgb.repositorio.UsuarioRepository;
import com.sgb.dto.CitaRespuestaDTO;
import com.sgb.dto.CitaSolicitudDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de negocio del modulo Cita.
 *
 * Aqui es donde se valida todo lo que el simple CRUD no puede garantizar
 * por si solo: que el cliente, el barbero y el servicio existan, que el
 * barbero de verdad tenga el rol de Barbero, y que la fecha de la cita no
 * sea en el pasado. Estas reglas ya estaban documentadas como parte del
 * analisis del proyecto (ver Evidencia GA2-220501093-AA1-EV04, seccion
 * "Modelo de dominio").
 *
 * id_rol = 2 corresponde a "Barbero" segun los datos reales de la tabla
 * `rol` (ver sgb.sql, INSERT INTO rol).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CitaService {

    private static final Integer ID_ROL_BARBERO = 2;
    private static final Integer ID_ROL_CLIENTE = 3;

    private final CitaRepository citaRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<CitaRespuestaDTO> listarTodas() {
        return citaRepository.findAll().stream()
                .map(CitaRespuestaDTO::desde)
                .toList();
    }

    public CitaRespuestaDTO buscarPorId(Integer id) {
        return CitaRespuestaDTO.desde(buscarEntidadPorId(id));
    }

    /** Todas las citas de un cliente especifico (su "historial"). */
    public List<CitaRespuestaDTO> listarPorCliente(Integer idCliente) {
        return citaRepository.findByCliente_IdUsuario(idCliente).stream()
                .map(CitaRespuestaDTO::desde)
                .toList();
    }

    /** Todas las citas asignadas a un barbero especifico (su "agenda"). */
    public List<CitaRespuestaDTO> listarPorBarbero(Integer idBarbero) {
        return citaRepository.findByBarbero_IdUsuario(idBarbero).stream()
                .map(CitaRespuestaDTO::desde)
                .toList();
    }

    private Cita buscarEntidadPorId(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una cita con id " + id));
    }

    public CitaRespuestaDTO crear(CitaSolicitudDTO solicitud) {
        Usuario cliente = buscarUsuario(solicitud.getIdCliente(), "cliente");
        validarQueSeaCliente(cliente);
        Usuario barbero = buscarUsuario(solicitud.getIdBarbero(), "barbero");
        validarQueSeaBarbero(barbero);
        Servicio servicio = buscarServicioActivo(solicitud.getIdServicio());
        validarFechaFutura(solicitud.getFechaHora());

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(solicitud.getFechaHora());
        cita.setNotas(solicitud.getNotas());
        cita.setEstado(EstadoCita.pendiente);

        return CitaRespuestaDTO.desde(citaRepository.save(cita));
    }

    public CitaRespuestaDTO actualizar(Integer id, CitaSolicitudDTO solicitud) {
        Cita cita = buscarEntidadPorId(id);
        Usuario cliente = buscarUsuario(solicitud.getIdCliente(), "cliente");
        validarQueSeaCliente(cliente);
        Usuario barbero = buscarUsuario(solicitud.getIdBarbero(), "barbero");
        validarQueSeaBarbero(barbero);
        Servicio servicio = buscarServicioActivo(solicitud.getIdServicio());
        validarFechaFutura(solicitud.getFechaHora());

        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(solicitud.getFechaHora());
        cita.setNotas(solicitud.getNotas());

        return CitaRespuestaDTO.desde(citaRepository.save(cita));
    }

    public CitaRespuestaDTO confirmar(Integer id) {
        Cita cita = buscarEntidadPorId(id);
        cita.confirmar();
        return CitaRespuestaDTO.desde(citaRepository.save(cita));
    }

    public CitaRespuestaDTO cancelar(Integer id) {
        Cita cita = buscarEntidadPorId(id);
        cita.cancelar();
        return CitaRespuestaDTO.desde(citaRepository.save(cita));
    }

    public CitaRespuestaDTO completar(Integer id) {
        Cita cita = buscarEntidadPorId(id);
        cita.completar();
        return CitaRespuestaDTO.desde(citaRepository.save(cita));
    }

    /**
     * Elimina la fila de la base de datos de verdad. Se deja disponible
     * porque la guia de la actividad pide las 4 operaciones basicas
     * (insertar, consultar, actualizar, eliminar), pero en la practica es
     * preferible usar cancelar() para no perder el historial de la cita.
     */
    public void eliminar(Integer id) {
        Cita cita = buscarEntidadPorId(id);
        citaRepository.delete(cita);
    }

    // ---- Validaciones privadas ----

    private Usuario buscarUsuario(Integer id, String rolEsperadoTexto) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe un usuario (" + rolEsperadoTexto + ") con id " + id));
    }

    private void validarQueSeaBarbero(Usuario barbero) {
        if (!ID_ROL_BARBERO.equals(barbero.getIdRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario con id " + barbero.getIdUsuario()
                            + " no tiene el rol de Barbero, no se le pueden asignar citas");
        }
    }

    private void validarQueSeaCliente(Usuario cliente) {
        if (!ID_ROL_CLIENTE.equals(cliente.getIdRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario con id " + cliente.getIdUsuario()
                            + " no tiene el rol de Cliente, no puede agendar citas como cliente");
        }
    }

    private Servicio buscarServicioActivo(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe un servicio con id " + id));
        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El servicio con id " + id + " esta desactivado, no se puede agendar");
        }
        return servicio;
    }

    private void validarFechaFutura(LocalDateTime fechaHora) {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha y hora de la cita no puede ser en el pasado");
        }
    }
}
