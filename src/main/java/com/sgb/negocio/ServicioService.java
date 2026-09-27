package com.sgb.negocio;

import com.sgb.modelo.Servicio;
import com.sgb.repositorio.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Logica de negocio del modulo Servicio. El controlador (ServicioController)
 * NO habla directamente con el repositorio: siempre pasa por esta clase,
 * que es la unica responsable de decidir que es una operacion valida.
 */
@Service
@RequiredArgsConstructor // Lombok genera el constructor con este campo final -> Spring lo inyecta solo.
@Transactional
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public List<Servicio> listar(boolean incluirInactivos) {
        return incluirInactivos
                ? servicioRepository.findAll()
                : servicioRepository.findByActivoTrue();
    }

    public Servicio buscarPorId(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un servicio con id " + id));
    }

    public Servicio crear(Servicio nuevo) {
        // El id lo genera la base de datos (AUTO_INCREMENT); si llega uno
        // desde la peticion, se ignora para evitar sobrescribir un
        // servicio existente por error.
        nuevo.setIdServicio(null);
        if (nuevo.getActivo() == null) {
            nuevo.setActivo(true);
        }
        return servicioRepository.save(nuevo);
    }

    public Servicio actualizar(Integer id, Servicio datos) {
        Servicio existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setDuracionMin(datos.getDuracionMin());
        if (datos.getActivo() != null) {
            existente.setActivo(datos.getActivo());
        }
        return servicioRepository.save(existente);
    }

    /**
     * "Elimina" un servicio SIN borrar la fila de la base de datos:
     * solo lo marca como inactivo (activo = false). Si se borrara de
     * verdad, cualquier cita antigua que ya haya usado ese servicio
     * quedaria con una referencia rota (id_servicio apuntando a nada), lo
     * que MySQL ademas impide por la restriccion de llave foranea
     * fk_cita_servicio definida en sgb.sql.
     */
    public void desactivar(Integer id) {
        Servicio existente = buscarPorId(id);
        existente.setActivo(false);
        servicioRepository.save(existente);
    }
}
