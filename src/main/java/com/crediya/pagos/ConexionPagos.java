package com.crediya.pagos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abre conexiones para el módulo de pagos.
 * Lee la configuración desde src/main/resources/db.properties.
 */
public class ConexionPagos {

    private ConexionPagos() {
        // Evita crear objetos de esta clase de utilidad.
    }

    public static Connection obtenerConexion() throws SQLException {
        Properties propiedades = new Properties();

        try (InputStream archivo = ConexionPagos.class.getResourceAsStream("/db.properties")) {
            if (archivo == null) {
                throw new SQLException(
                        "No se encontró db.properties. Crea el archivo en src/main/resources "
                        + "usando db.properties.example como guía.");
            }
            propiedades.load(archivo);
        } catch (IOException e) {
            throw new SQLException("No fue posible leer db.properties.", e);
        }

        String url = obtenerPropiedad(propiedades, "db.url", "url");
        String usuario = obtenerPropiedad(propiedades, "db.user", "db.username", "user", "username");
        String clave = obtenerPropiedad(propiedades, "db.password", "password");

        if (url == null || usuario == null || clave == null) {
            throw new SQLException(
                    "La configuración de conexión está incompleta. Revisa db.url, db.user y db.password.");
        }

        return DriverManager.getConnection(url, usuario, clave);
    }

    private static String obtenerPropiedad(Properties propiedades, String... nombres) {
        for (String nombre : nombres) {
            String valor = propiedades.getProperty(nombre);
            if (valor != null && !valor.trim().isEmpty()) {
                return valor.trim();
            }
        }
        return null;
    }
}
