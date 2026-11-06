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
 * CLASE: ClienteDAO
 *
 * DAO significa Data Access Object.
 *
 * Esta clase se encarga de realizar las operaciones
 * de acceso a la tabla clientes de MySQL.
 *
 * Aquí implementamos las operaciones CRUD:
 * insertar, listar, buscar, actualizar y eliminar.
 */
public class ClienteDAO {

    /*
     * MÉTODO INSERTAR
     *
     * Registra un nuevo cliente en la base de datos.
     *
     * Utilizamos PreparedStatement para ejecutar la consulta
     * SQL y enviar los datos del cliente mediante parámetros.
     */
    public boolean insertar(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO clientes "
                + "(nombre, documento, correo, telefono) "
                + "VALUES (?, ?, ?, ?)";

        // Establece la conexión y prepara la consulta SQL.
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asigna los datos del cliente a los signos de interrogación.
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getDocumento());
            sentencia.setString(3, cliente.getCorreo());
            sentencia.setString(4, cliente.getTelefono());

            // Ejecuta la consulta de inserción.
            int filasAfectadas = sentencia.executeUpdate();

            // Comprueba si el cliente fue registrado.
            if (filasAfectadas > 0) {

                // Recupera el ID generado automáticamente por MySQL.
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
     * MÉTODO LISTAR
     *
     * Consulta todos los clientes registrados en MySQL.
     *
     * ArrayList permite almacenar los clientes encontrados
     * para devolverlos y mostrarlos posteriormente.
     */
    public ArrayList<Cliente> listar() throws SQLException {

        ArrayList<Cliente> listaClientes = new ArrayList<>();

        String sql = "SELECT id, nombre, documento, correo, telefono "
                + "FROM clientes ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            // Recorre los registros devueltos por MySQL.
            while (resultado.next()) {

                // Crea un objeto Cliente con los datos encontrados.
                Cliente cliente = new Cliente(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("documento"),
                        resultado.getString("correo"),
                        resultado.getString("telefono")
                );

                // Agrega el cliente a la lista.
                listaClientes.add(cliente);
            }
        }

        return listaClientes;
    }


    /*
     * MÉTODO BUSCAR POR ID
     *
     * Busca un cliente específico utilizando su identificador.
     *
     * Devuelve el objeto Cliente si lo encuentra.
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
     * MÉTODO ACTUALIZAR
     *
     * Modifica la información de un cliente existente.
     *
     * El ID permite identificar qué registro debe actualizarse.
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

            // Devuelve true si MySQL informa que se modificaron filas.
            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * MÉTODO ELIMINAR
     *
     * Elimina un cliente utilizando su identificador.
     *
     * Si el cliente tiene préstamos relacionados, MySQL puede
     * impedir la eliminación debido a la clave foránea.
     */
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            // Devuelve true si se eliminó algún registro.
            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Qué significa DAO?
     * Respuesta: Data Access Object. Es una clase encargada
     * de realizar las operaciones de acceso a la base de datos.
     *
     * 2. ¿Para qué sirve ClienteDAO?
     * Respuesta: Para administrar los registros de clientes
     * almacenados en MySQL.
     *
     * 3. ¿Qué significa CRUD?
     * Respuesta: Create, Read, Update y Delete.
     * Son las operaciones básicas para administrar datos.
     *
     * 4. ¿Para qué sirve PreparedStatement?
     * Respuesta: Para preparar y ejecutar consultas SQL
     * utilizando parámetros.
     *
     * 5. ¿Qué significan los signos de interrogación (?)?
     * Respuesta: Son espacios reservados donde se asignan
     * los valores que se enviarán a la consulta SQL.
     *
     * 6. ¿Para qué sirve ResultSet?
     * Respuesta: Para recorrer y consultar los resultados
     * obtenidos de una consulta SELECT.
     *
     * 7. ¿Para qué sirve ArrayList?
     * Respuesta: Para almacenar varios objetos Cliente
     * en una lista.
     *
     * 8. ¿Qué significa executeUpdate()?
     * Respuesta: Ejecuta operaciones como INSERT, UPDATE
     * y DELETE, y devuelve la cantidad de filas afectadas.
     *
     * 9. ¿Para qué sirve executeQuery()?
     * Respuesta: Para ejecutar consultas SELECT
     * y obtener sus resultados mediante ResultSet.
     *
     * 10. ¿Qué significa throws SQLException?
     * Respuesta: Indica que el método puede producir
     * un error relacionado con las operaciones SQL.
     *
     * 11. ¿Para qué sirve try-with-resources?
     * Respuesta: Permite cerrar automáticamente los recursos
     * utilizados, como conexiones y consultas.
     *
     * 12. ¿Para qué sirve RETURN_GENERATED_KEYS?
     * Respuesta: Permite recuperar el identificador que
     * MySQL genera automáticamente al insertar un cliente.
     *
     * 13. ¿Por qué utilizamos WHERE id = ?
     * Respuesta: Para identificar el cliente específico
     * que queremos consultar, actualizar o eliminar.
     *
     * 14. ¿Por qué MySQL podría impedir eliminar un cliente?
     * Respuesta: Porque puede tener préstamos relacionados
     * mediante una clave foránea.
     *
     * =====================================================
     */
}