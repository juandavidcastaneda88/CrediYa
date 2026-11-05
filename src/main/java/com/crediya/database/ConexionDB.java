
package com.crediya.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Administra las conexiones a la base de datos CrediYa.
 */
public final class ConexionDB {

    private static final String ARCHIVO_CONFIGURACION = "/db.properties";

    private ConexionDB() {
    }

    /**
     * Establece una conexión con MySQL.
     *
     * @return conexión activa con la base de datos.
     * @throws SQLException si ocurre un error de conexión.
     */
    public static Connection obtenerConexion() throws SQLException {

        Properties propiedades = cargarConfiguracion();

        String url = obtenerPropiedad(propiedades, "db.url");
        String usuario = obtenerPropiedad(propiedades, "db.username");
        String contrasena = obtenerPropiedad(propiedades, "db.password");

        return DriverManager.getConnection(url, usuario, contrasena);
    }

    /**
     * Carga las propiedades de conexión.
     *
     * @return propiedades de configuración.
     */
    private static Properties cargarConfiguracion() {

        Properties propiedades = new Properties();

        try (InputStream entrada = ConexionDB.class.getResourceAsStream(
                ARCHIVO_CONFIGURACION)) {

            if (entrada == null) {
                throw new IllegalStateException(
                        "No se encontró el archivo db.properties."
                );
            }

            propiedades.load(entrada);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo cargar la configuración de MySQL.", e
            );
        }

        return propiedades;
    }

    /**
     * Obtiene una propiedad obligatoria.
     *
     * @param propiedades configuración cargada.
     * @param clave nombre de la propiedad.
     * @return valor de la propiedad.
     */
    private static String obtenerPropiedad(
            Properties propiedades, String clave) {

        String valor = propiedades.getProperty(clave);

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Falta configurar la propiedad: " + clave
            );
        }

        return valor;
    }

    /**
     * Comprueba que MySQL acepte la conexión.
     *
     * @return true si la conexión es válida.
     * @throws SQLException si ocurre un error.
     */
    public static boolean probarConexion() throws SQLException {

        try (Connection conexion = obtenerConexion()) {
            return conexion.isValid(5);
        }
    }
}
