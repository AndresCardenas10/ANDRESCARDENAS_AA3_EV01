package com.sgb.web;

import com.sgb.dto.CitaRespuestaDTO;
import com.sgb.dto.CitaSolicitudDTO;
import com.sgb.negocio.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expone el modulo Cita como una API REST.
 *
 * Reutiliza el modulo de Usuario/login ya construido en la Evidencia
 * AA2-EV02 (solo lee de la tabla `usuario`, no la modifica) y el modulo
 * Servicio de esta misma evidencia.
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    /** GET /api/citas -> lista todas las citas. */
    @GetMapping
    public List<CitaRespuestaDTO> listar() {
        return citaService.listarTodas();
    }

    /** GET /api/citas/{id} */
    @GetMapping("/{id}")
    public CitaRespuestaDTO consultar(@PathVariable Integer id) {
        return citaService.buscarPorId(id);
    }

    /** GET /api/citas/cliente/{idCliente} -> historial de citas de un cliente. */
    @GetMapping("/cliente/{idCliente}")
    public List<CitaRespuestaDTO> listarPorCliente(@PathVariable Integer idCliente) {
        return citaService.listarPorCliente(idCliente);
    }

    /** GET /api/citas/barbero/{idBarbero} -> agenda de citas de un barbero. */
    @GetMapping("/barbero/{idBarbero}")
    public List<CitaRespuestaDTO> listarPorBarbero(@PathVariable Integer idBarbero) {
        return citaService.listarPorBarbero(idBarbero);
    }

    /** POST /api/citas -> agenda una cita nueva (queda en estado "pendiente"). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaRespuestaDTO crear(@Valid @RequestBody CitaSolicitudDTO solicitud) {
        return citaService.crear(solicitud);
    }

    /** PUT /api/citas/{id} -> reprograma una cita (fecha, notas, etc.). */
    @PutMapping("/{id}")
    public CitaRespuestaDTO actualizar(@PathVariable Integer id, @Valid @RequestBody CitaSolicitudDTO solicitud) {
        return citaService.actualizar(id, solicitud);
    }

    /** PUT /api/citas/{id}/confirmar -> pasa la cita a estado "confirmada". */
    @PutMapping("/{id}/confirmar")
    public CitaRespuestaDTO confirmar(@PathVariable Integer id) {
        return citaService.confirmar(id);
    }

    /** PUT /api/citas/{id}/cancelar -> pasa la cita a estado "cancelada". */
    @PutMapping("/{id}/cancelar")
    public CitaRespuestaDTO cancelar(@PathVariable Integer id) {
        return citaService.cancelar(id);
    }

    /** PUT /api/citas/{id}/completar -> pasa la cita a estado "completada". */
    @PutMapping("/{id}/completar")
    public CitaRespuestaDTO completar(@PathVariable Integer id) {
        return citaService.completar(id);
    }

    /**
     * DELETE /api/citas/{id} -> borra la cita de verdad. Se deja
     * disponible por completitud (la guia pide la operacion "eliminar"),
     * pero para el uso normal del sistema es mejor usar
     * PUT /api/citas/{id}/cancelar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
