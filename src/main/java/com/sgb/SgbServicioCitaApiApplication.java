package com.sgb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del proyecto.
 *
 * Evidencia GA7-220501096-AA3-EV01 - Codificacion de modulos del software.
 *
 * Al ejecutar esta clase, Spring Boot levanta un servidor web embebido
 * (Tomcat) en el puerto configurado en application.properties (8081) y
 * deja disponibles los endpoints de los modulos Servicio y Cita.
 *
 * A diferencia del proyecto de la Evidencia AA2-EV02 (Servlets + JSP,
 * empaquetado como .war para desplegar en un Tomcat externo), aqui no
 * hace falta instalar ni configurar ningun servidor aparte: Spring Boot
 * lo trae incluido. Basta con ejecutar esta clase (o el comando
 * "mvn spring-boot:run") para que la API quede funcionando.
 */
@SpringBootApplication
public class SgbServicioCitaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgbServicioCitaApiApplication.class, args);
    }
}
