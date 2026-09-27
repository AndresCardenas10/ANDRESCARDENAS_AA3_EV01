# Módulos Servicio y Cita — API REST (SGB)
Evidencia **GA7-220501096-AA3-EV01** — Codificación de módulos del software

> Nota sobre la referencia de esta evidencia: este README corresponde a
> **AA3-EV01**. (En la Evidencia anterior, AA2-EV02, el instructor señaló
> que en parte de la documentación seguía apareciendo "EV01" por error;
> aquí se revisó con cuidado que la numeración fuera consistente en todo
> el documento.)

## Qué hace este proyecto

Una **API REST** (con Spring Boot y Spring Data JPA) para los módulos
**Servicio** y **Cita** del proyecto Sistema Gestión Barbería (SGB):

| Módulo   | Operaciones                                                             |
|----------|--------------------------------------------------------------------------|
| Servicio | crear, listar (activos o todos), consultar por id, actualizar, desactivar |
| Cita     | crear, listar (todas / por cliente / por barbero), consultar por id, actualizar, confirmar, cancelar, completar, eliminar |

## Por qué un proyecto nuevo y no una modificación del proyecto Servlets/JSP

El proyecto ya entregado y calificado en la Evidencia **GA7-220501096-AA2-EV01/EV02**
(carpeta con `pom.xml` de tipo `war`) está construido con Servlets + JSP +
JDBC puro, sin ninguna dependencia de Spring. Para aplicar el framework
que se vio en las clases de apoyo (Spring Boot + Spring Data JPA) sin
arriesgar romper ese proyecto que ya funciona, esta evidencia se
construyó como un **proyecto Maven independiente**, dentro del mismo
repositorio, en su propia carpeta (`sgb-servicio-cita-api/`). Ambos
proyectos comparten la misma base de datos MySQL (`sgb`).

## Esquema de datos usado

Las entidades `Servicio` y `Cita` están mapeadas **exactamente** a las
tablas `servicio` y `cita` que ya existen en la base de datos real `sgb`
(ver `sql/sgb.sql` en la Evidencia AA2-EV02, que es un volcado literal de
esa base de datos):

- **`servicio`**: `id_servicio, nombre, descripcion, precio, duracion_min, activo`
- **`cita`**: `id_cita, id_cliente, id_barbero, id_servicio, fecha_hora, estado, expira_en, notas, creado_en`

El proyecto **no crea ni modifica** estas tablas (`spring.jpa.hibernate.ddl-auto=validate`):
solo se conecta a la base de datos que ya existe.

> Aclaración sobre el modelo de datos: durante el análisis del proyecto se
> generaron varias versiones del modelo (el diagrama de clases de la
> Evidencia GA4-220501095-AA2-EV04, con entidades Cliente/Barbero/Factura,
> y el modelo lógico inicial GA4-220501095-AA1-EV02). Ese diseño evolucionó
> en la práctica hacia una tabla `usuario` única con roles, sin módulo de
> facturación, que es la que realmente se creó y se está usando en la base
> de datos. Este proyecto sigue esa versión real para no generar errores
> de conexión, y retoma del diagrama de clases únicamente los nombres de
> los métodos de negocio (`agendar()`, `cancelar()`, `completar()`, etc.),
> que sí se pueden aplicar sin depender de la estructura de tablas.

## Estructura del proyecto

```
sgb-servicio-cita-api/
├── pom.xml
├── README.md                          (este archivo)
├── ENLACE_REPOSITORIO.txt
└── src/main/
    ├── java/com/sgb/
    │   ├── SgbServicioCitaApiApplication.java   (clase principal)
    │   ├── modelo/
    │   │   ├── Servicio.java
    │   │   ├── Cita.java
    │   │   ├── EstadoCita.java
    │   │   └── Usuario.java           (solo lectura; reutiliza la tabla del login)
    │   ├── repositorio/
    │   │   ├── ServicioRepository.java
    │   │   ├── CitaRepository.java
    │   │   └── UsuarioRepository.java
    │   ├── negocio/
    │   │   ├── ServicioService.java
    │   │   └── CitaService.java
    │   ├── dto/
    │   │   ├── CitaSolicitudDTO.java
    │   │   └── CitaRespuestaDTO.java
    │   └── web/
    │       ├── ServicioController.java
    │       └── CitaController.java
    └── resources/application.properties
```

**Convenciones de codificación aplicadas** (las mismas de la Evidencia
AA2-EV01/EV02): clases en `PascalCase`, métodos y variables en
`camelCase`, paquetes en minúscula.

## Endpoints disponibles

### Servicio

| Método | Endpoint                  | Qué hace                                   |
|--------|----------------------------|---------------------------------------------|
| GET    | `/api/servicios`           | Lista los servicios activos                 |
| GET    | `/api/servicios?todos=true`| Lista todos, incluidos los desactivados     |
| GET    | `/api/servicios/{id}`      | Consulta un servicio por id                 |
| POST   | `/api/servicios`           | Crea un servicio nuevo                      |
| PUT    | `/api/servicios/{id}`      | Actualiza un servicio                       |
| DELETE | `/api/servicios/{id}`      | Desactiva un servicio (no lo borra)         |

### Cita

| Método | Endpoint                        | Qué hace                                |
|--------|-----------------------------------|-------------------------------------------|
| GET    | `/api/citas`                      | Lista todas las citas                     |
| GET    | `/api/citas/{id}`                 | Consulta una cita por id                  |
| GET    | `/api/citas/cliente/{idCliente}`  | Historial de citas de un cliente          |
| GET    | `/api/citas/barbero/{idBarbero}`  | Agenda de citas de un barbero             |
| POST   | `/api/citas`                      | Agenda una cita nueva (queda "pendiente") |
| PUT    | `/api/citas/{id}`                 | Reprograma una cita                       |
| PUT    | `/api/citas/{id}/confirmar`       | Cambia el estado a "confirmada"           |
| PUT    | `/api/citas/{id}/cancelar`        | Cambia el estado a "cancelada"            |
| PUT    | `/api/citas/{id}/completar`       | Cambia el estado a "completada"           |
| DELETE | `/api/citas/{id}`                 | Elimina la cita de la base de datos       |

## Cómo ejecutarlo (resumen técnico)

La guía completa, paso a paso y sin dar por hecho ningún conocimiento
previo, está en el documento de la Etapa 4 de esta evidencia. En resumen:

1. Tener XAMPP con MySQL corriendo y la base `sgb` ya importada (la misma
   que se usa en la Evidencia AA2-EV02).
2. Abrir esta carpeta como proyecto Maven en NetBeans (u otro IDE).
3. Ejecutar la clase `SgbServicioCitaApiApplication`.
4. Abrir `http://localhost:8081/probador.html` en el navegador: es una
   página de prueba incluida en el proyecto (`src/main/resources/static/probador.html`)
   con botones y formularios para probar los endpoints de Servicio y
   Cita (crear, listar, cambiar estado, etc.) sin instalar Postman ni
   nada adicional.

La API queda disponible en `http://localhost:8081`.

## Decisiones de diseño (y por qué)

- **Proyecto Spring Boot separado** del proyecto Servlets/JSP existente,
  para no arriesgar lo ya calificado (ver sección anterior).
- **Baja lógica (soft delete) en Servicio**: en vez de borrar la fila,
  `DELETE /api/servicios/{id}` pone `activo = false`. Borrar de verdad
  rompería las citas antiguas que ya usaron ese servicio (la base de
  datos ni lo permite: hay una restricción de llave foránea).
- **DTOs para Cita, entidad directa para Servicio**: `Cita` tiene
  relaciones (`@ManyToOne` a `Usuario` y `Servicio`) que Hibernate no
  carga automáticamente al convertir a JSON (`LazyInitializationException`),
  así que se usa un DTO que copia los datos ya resueltos. `Servicio` no
  tiene relaciones, así que no tiene ese problema y se usa la entidad
  directamente, sin una capa adicional innecesaria.
- **Validaciones de negocio en `CitaService`**: no se puede agendar una
  cita con un servicio desactivado, con un usuario que no exista, con un
  "barbero" que en realidad tenga otro rol, ni con una fecha en el
  pasado. Estas reglas ya estaban anotadas en el análisis del proyecto
  (Evidencia GA2-220501093-AA1-EV04, sección "Modelo de dominio").
- **`ddl-auto=validate` y no `update`**: la base de datos `sgb` ya existe
  con usuarios y roles de prueba reales; no se quiere que Hibernate
  intente alterarla sola.

## Pruebas ejecutadas y resultados

Pruebas ejecutadas el 27 de septiembre de 2026, sobre la base de datos
real `sgb` (MySQL/XAMPP, puerto 3307), usando la página `probador.html`
incluida en el proyecto. Esto corrige, además, la observación puntual
del instructor en la Evidencia AA2-EV02 sobre documentar en el README
las pruebas ejecutadas y sus resultados.

| # | Prueba | Resultado esperado | Resultado obtenido |
|---|--------|---------------------|----------------------|
| 1 | Arranque de la aplicación (`spring-boot:run`) | Hibernate valida las entidades contra las tablas reales sin errores; el servidor inicia en el puerto 8081 | ✅ Arrancó correctamente ("Started SgbServicioCitaApiApplication"), conexión a MySQL establecida (HikariCP) y validación del esquema exitosa |
| 2 | `GET /api/servicios` | Devuelve la lista de servicios activos | ✅ Responde correctamente con la lista |
| 3 | `POST /api/servicios` | Crea un servicio nuevo, responde 201 | ✅ Servicio creado correctamente |
| 4 | `POST /api/citas` con un usuario que no tiene el rol de Barbero | Responde 400 con un mensaje que explique la causa | ✅ Rechazada con HTTP 400 y el mensaje "...no tiene el rol de Barbero, no se le pueden asignar citas" |
| 5 | `POST /api/citas` con cliente, barbero y servicio válidos | Agenda la cita, responde 201, estado inicial "pendiente" | ✅ Citas agendadas correctamente (ids 2 y 3) |
| 6 | `PUT /api/citas/{id}/completar` | Cambia el estado de la cita a "completada" | ✅ La cita de Laura Martínez con Carlos Ramírez (Corte de cabello) cambió a "completada" |
| 7 | `PUT /api/citas/{id}/cancelar` | Cambia el estado de la cita a "cancelada" | ✅ La cita de Santiago Pérez con Julián Gómez (Afeitado clásico) cambió a "cancelada" |
| 8 | `GET /api/citas/{id}` con un id que no existe | Responde 404 con un mensaje claro | ✅ Probado con un id inexistente: respondió 404 "Not Found" |
| 9 | `GET /api/citas` (listar todas) | Devuelve las citas con los datos de cliente, barbero y servicio ya resueltos (no solo ids) | ✅ Confirmado: la respuesta trae los nombres completos (ej. "Laura Martinez", "Carlos Ramirez") gracias al DTO `CitaRespuestaDTO` |
