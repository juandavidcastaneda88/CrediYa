
package com.crediya.dao;

import com.crediya.database.ConexionDB;
import com.crediya.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * =====================================================
 * CLASE: ClienteDAO
 * =====================================================
 *
 * DAO significa Data Access Object.
 *
 * Esta clase realiza las operaciones de acceso
 * a la tabla clientes de MySQL.
 *
 * Implementa las operaciones CRUD:
 * insertar, listar, buscar, actualizar y eliminar.
 */
public class ClienteDAO {

    /*
     * METODO INSERTAR
     *
     * Registra un nuevo cliente en MySQL.
     */
    public boolean insertar(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO clientes "
                + "(nombre, documento, correo, telefono) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getDocumento());
            sentencia.setString(3, cliente.getCorreo());
            sentencia.setString(4, cliente.getTelefono());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves = sentencia.getGeneratedKeys()) {

                    if (claves.next()) {
                        cliente.setId(claves.getInt(1));
                    }
                }

                return true;
            }

            return false;
        }
    }


    /*
     * METODO LISTAR
     *
     * Consulta todos los clientes registrados.
     */
    public ArrayList<Cliente> listar() throws SQLException {

        ArrayList<Cliente> listaClientes = new ArrayList<>();

        String sql = "SELECT id, nombre, documento, correo, telefono "
                + "FROM clientes ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                Cliente cliente = new Cliente(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("documento"),
                        resultado.getString("correo"),
                        resultado.getString("telefono")
                );

                listaClientes.add(cliente);
            }
        }

        return listaClientes;
    }


    /*
     * METODO BUSCAR POR ID
     *
     * Busca un cliente mediante su identificador.
     *
     * Si existe, devuelve el objeto Cliente.
     * Si no existe, devuelve null.
     */
    public Cliente buscarPorId(int id) throws SQLException {

        String sql = "SELECT id, nombre, documento, correo, telefono "
                + "FROM clientes WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    return new Cliente(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("documento"),
                            resultado.getString("correo"),
                            resultado.getString("telefono")
                    );
                }
            }
        }

        return null;
    }


    /*
     * METODO ACTUALIZAR
     *
     * Modifica los datos de un cliente existente.
     */
    public boolean actualizar(Cliente cliente) throws SQLException {

        String sql = "UPDATE clientes SET "
                + "nombre = ?, documento = ?, correo = ?, telefono = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getDocumento());
            sentencia.setString(3, cliente.getCorreo());
            sentencia.setString(4, cliente.getTelefono());
            sentencia.setInt(5, cliente.getId());

            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * METODO ELIMINAR
     *
     * Elimina un cliente utilizando su identificador.
     *
     * MySQL puede impedir la eliminacion si el cliente
     * tiene prestamos relacionados mediante una clave foranea.
     */
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Que significa DAO?
     * Respuesta: Data Access Object. Es una clase encargada
     * de realizar operaciones de acceso a la base de datos.
     *
     * 2. ¿Para que sirve ClienteDAO?
     * Respuesta: Para administrar los registros de clientes
     * almacenados en MySQL.
     *
     * 3. ¿Que significa CRUD?
     * Respuesta: Create, Read, Update y Delete.
     *
     * 4. ¿Para que sirve PreparedStatement?
     * Respuesta: Para preparar y ejecutar consultas SQL
     * utilizando parametros.
     *
     * 5. ¿Para que sirve ResultSet?
     * Respuesta: Para recorrer los resultados de una consulta.
     *
     * 6. ¿Para que sirve ArrayList?
     * Respuesta: Para almacenar varios objetos Cliente.
     *
     * 7. ¿Para que sirve executeUpdate()?
     * Respuesta: Para ejecutar INSERT, UPDATE y DELETE.
     *
     * 8. ¿Para que sirve executeQuery()?
     * Respuesta: Para ejecutar consultas SELECT.
     *
     * 9. ¿Que significa throws SQLException?
     * Respuesta: Indica que el metodo puede generar
     * un error relacionado con operaciones SQL.
     *
     * 10. ¿Para que sirve try-with-resources?
     * Respuesta: Para cerrar automaticamente conexiones
     * y otros recursos utilizados.
     *
     * 11. ¿Para que sirve RETURN_GENERATED_KEYS?
     * Respuesta: Para recuperar el ID generado por MySQL.
     *
     * 12. ¿Por que utilizamos WHERE id = ?
     * Respuesta: Para identificar el cliente que queremos
     * consultar, actualizar o eliminar.
     *
     * =====================================================
     */
}
