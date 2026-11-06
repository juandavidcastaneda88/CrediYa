
package com.crediya.dao;

import com.crediya.database.ConexionDB;
import com.crediya.model.Empleado;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * CLASE: EmpleadoDAO
 *
 * DAO significa Data Access Object.
 *
 * Esta clase realiza las operaciones de acceso
 * a la tabla empleados de la base de datos MySQL.
 *
 * Implementa las operaciones CRUD:
 * insertar, listar, buscar, actualizar y eliminar.
 */
public class EmpleadoDAO {

    /*
     * MÉTODO INSERTAR
     *
     * Registra un nuevo empleado en la base de datos.
     */
    public boolean insertar(Empleado empleado) throws SQLException {

        String sql = "INSERT INTO empleados "
                + "(nombre, documento, rol, correo, salario) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asigna los datos del empleado a la consulta SQL.
            sentencia.setString(1, empleado.getNombre());
            sentencia.setString(2, empleado.getDocumento());
            sentencia.setString(3, empleado.getRol());
            sentencia.setString(4, empleado.getCorreo());
            sentencia.setDouble(5, empleado.getSalario());

            // Ejecuta la inserción.
            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                // Recupera el ID generado automáticamente por MySQL.
                try (ResultSet claves = sentencia.getGeneratedKeys()) {

                    if (claves.next()) {
                        empleado.setId(claves.getInt(1));
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
     * Consulta todos los empleados registrados.
     *
     * ArrayList almacena los objetos Empleado encontrados.
     */
    public ArrayList<Empleado> listar() throws SQLException {

        ArrayList<Empleado> listaEmpleados = new ArrayList<>();

        String sql = "SELECT id, nombre, documento, rol, correo, salario "
                + "FROM empleados ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            // Recorre los registros encontrados en MySQL.
            while (resultado.next()) {

                // Crea un objeto Empleado con los datos obtenidos.
                Empleado empleado = new Empleado(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("documento"),
                        resultado.getString("correo"),
                        resultado.getString("rol"),
                        resultado.getDouble("salario")
                );

                // Agrega el empleado a la lista.
                listaEmpleados.add(empleado);
            }
        }

        return listaEmpleados;
    }


    /*
     * MÉTODO BUSCAR POR ID
     *
     * Busca un empleado utilizando su identificador.
     *
     * Si lo encuentra, devuelve el objeto Empleado.
     * Si no existe, devuelve null.
     */
    public Empleado buscarPorId(int id) throws SQLException {

        String sql = "SELECT id, nombre, documento, rol, correo, salario "
                + "FROM empleados WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    return new Empleado(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("documento"),
                            resultado.getString("correo"),
                            resultado.getString("rol"),
                            resultado.getDouble("salario")
                    );
                }
            }
        }

        return null;
    }


    /*
     * MÉTODO ACTUALIZAR
     *
     * Modifica los datos de un empleado existente.
     *
     * El ID permite identificar el registro que se actualizará.
     */
    public boolean actualizar(Empleado empleado) throws SQLException {

        String sql = "UPDATE empleados SET "
                + "nombre = ?, documento = ?, rol = ?, "
                + "correo = ?, salario = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, empleado.getNombre());
            sentencia.setString(2, empleado.getDocumento());
            sentencia.setString(3, empleado.getRol());
            sentencia.setString(4, empleado.getCorreo());
            sentencia.setDouble(5, empleado.getSalario());
            sentencia.setInt(6, empleado.getId());

            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * MÉTODO ELIMINAR
     *
     * Elimina un empleado utilizando su identificador.
     *
     * MySQL puede impedir la eliminación si el empleado
     * tiene préstamos asociados mediante una clave foránea.
     */
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM empleados WHERE id = ?";

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
     * 1. ¿Qué significa DAO?
     * Respuesta: Data Access Object. Es una clase que
     * realiza operaciones de acceso a la base de datos.
     *
     * 2. ¿Para qué sirve EmpleadoDAO?
     * Respuesta: Para administrar los empleados registrados
     * en la base de datos.
     *
     * 3. ¿Qué significa CRUD?
     * Respuesta: Create, Read, Update y Delete.
     * Son las operaciones básicas para administrar datos.
     *
     * 4. ¿Para qué sirve PreparedStatement?
     * Respuesta: Para ejecutar consultas SQL utilizando
     * parámetros.
     *
     * 5. ¿Para qué sirven los signos de interrogación (?)?
     * Respuesta: Son espacios reservados donde se asignan
     * los valores que se envían a la consulta SQL.
     *
     * 6. ¿Para qué sirve ResultSet?
     * Respuesta: Para recorrer y consultar los resultados
     * de una consulta SELECT.
     *
     * 7. ¿Para qué sirve ArrayList?
     * Respuesta: Para almacenar varios objetos Empleado
     * en una lista.
     *
     * 8. ¿Qué hace executeUpdate()?
     * Respuesta: Ejecuta operaciones INSERT, UPDATE
     * y DELETE y devuelve las filas afectadas.
     *
     * 9. ¿Qué hace executeQuery()?
     * Respuesta: Ejecuta consultas SELECT y devuelve
     * los resultados mediante ResultSet.
     *
     * 10. ¿Por qué utilizamos BigDecimal?
     * Respuesta: Para manejar el salario con mayor
     * precisión en los valores decimales.
     *
     * 11. ¿Para qué sirve setBigDecimal()?
     * Respuesta: Para enviar un valor BigDecimal
     * como parámetro de una consulta SQL.
     *
     * 12. ¿Para qué sirve getBigDecimal()?
     * Respuesta: Para recuperar un valor decimal
     * desde los resultados de MySQL.
     *
     * 13. ¿Qué significa throws SQLException?
     * Respuesta: Indica que el método puede producir
     * un error durante una operación con la base de datos.
     *
     * 14. ¿Para qué sirve WHERE id = ?
     * Respuesta: Para identificar el empleado específico
     * que queremos consultar, actualizar o eliminar.
     *
     * 15. ¿Por qué MySQL podría impedir eliminar
     * un empleado?
     * Respuesta: Porque puede tener préstamos asociados
     * mediante una clave foránea.
     *
     * =====================================================
     */
}
