package com.sgb.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un servicio que ofrece la barberia (por ejemplo
 * "Corte de cabello" o "Afeitado clasico").
 *
 * Las columnas de esta clase coinciden EXACTAMENTE con la tabla `servicio`
 * que ya existe en la base de datos real `sgb` (ver sql/sgb_estructura.sql
 * y sql/sgb_servicios_prueba.sql de la Evidencia AA2-EV02). No se usan los
 * nombres de columna de otros documentos de diseno del proyecto (como el
 * diagrama de clases de la Evidencia GA4-220501095-AA2-EV04, que usa
 * nombres distintos como "nombre_servicio" o "precio_estandar") porque esa
 * tabla nunca se llego a crear asi en la base de datos real: usar esos
 * nombres aqui haria que Hibernate fallara al validar la entidad contra la
 * base de datos real.
 */
@Entity
@Table(name = "servicio")
@Data
@NoArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "La duracion en minutos es obligatoria")
    @Positive(message = "La duracion debe ser mayor que cero")
    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin;

    /**
     * En vez de borrar un servicio de la base de datos (lo que rompería
     * las citas antiguas que ya lo usaron), se "desactiva" poniendo este
     * campo en false. Es el mismo patron que ya usa la tabla `usuario`
     * (columna `activo`) y la tabla `producto`.
     */
    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Metodo de negocio inspirado en el diagrama de clases de la
     * Evidencia GA4-220501095-AA2-EV04 (calcularPrecioFinal()). Por ahora
     * no hay descuentos ni recargos definidos en las reglas del negocio,
     * asi que devuelve el precio tal cual; queda aqui como el punto exacto
     * donde se agregaria esa logica en el futuro (por ejemplo, un
     * descuento por promocion) sin tener que tocar el controlador.
     */
    public BigDecimal calcularPrecioFinal() {
        return this.precio;
    }
}
