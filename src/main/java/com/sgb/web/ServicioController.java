package com.sgb.web;

import com.sgb.modelo.Servicio;
import com.sgb.negocio.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expone el modulo Servicio como una API REST.
 *
 * "REST" aqui significa: cada operacion es una combinacion de un verbo
 * HTTP (GET, POST, PUT, DELETE) y una URL (/api/servicios, .../{id}), en
 * vez del menu de texto por consola que usaba el proyecto de la
 * Evidencia AA2-EV01, o las paginas JSP con formularios de AA2-EV02.
 * Los pasos exactos para probar cada uno de estos endpoints (que
 * programa instalar, que escribir, que deberia aparecer) se explican en
 * la guia de ejecucion (Etapa 4).
 */
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    /** GET /api/servicios          -> solo servicios activos. */
    /** GET /api/servicios?todos=true -> incluye tambien los desactivados. */
    @GetMapping
    public List<Servicio> listar(@RequestParam(name = "todos", defaultValue = "false") boolean todos) {
        return servicioService.listar(todos);
    }

    /** GET /api/servicios/{id} */
    @GetMapping("/{id}")
    public Servicio consultar(@PathVariable Integer id) {
        return servicioService.buscarPorId(id);
    }

    /** POST /api/servicios -> crea un servicio nuevo. Responde 201 (Created). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servicio crear(@Valid @RequestBody Servicio servicio) {
        return servicioService.crear(servicio);
    }

    /** PUT /api/servicios/{id} -> actualiza un servicio existente. */
    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Integer id, @Valid @RequestBody Servicio servicio) {
        return servicioService.actualizar(id, servicio);
    }

    /**
     * DELETE /api/servicios/{id} -> desactiva el servicio (no lo borra
     * de la base de datos). Responde 204 (No Content): la operacion salio
     * bien y no hay nada mas que devolver.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        servicioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
