package com.crediya.dao;

import com.crediya.database.ConexionDB;
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * CLASE: PrestamoDAO
 *
 * DAO significa Data Access Object.
 *
 * Esta clase realiza las operaciones de acceso
 * a la tabla prestamos de la base de datos MySQL.
 *
 * Permite registrar, consultar, actualizar y eliminar
 * los préstamos de CrediYa S.A.S.
 */
public class PrestamoDAO {

    /*
     * MÉTODO INSERTAR
     *
     * Registra un préstamo en la base de datos.
     *
     * El préstamo debe tener un cliente y un empleado
     * previamente registrados en MySQL.
     */
    public boolean insertar(Prestamo prestamo) throws SQLException {

        String sql = "INSERT INTO prestamos "
                + "(cliente_id, empleado_id, monto, interes, cuotas, "
                + "fecha_inicio, fecha_vencimiento, monto_total, "
                + "valor_cuota, saldo_pendiente, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setInt(1, prestamo.getCliente().getId());
            sentencia.setInt(2, prestamo.getEmpleado().getId());
            sentencia.setDouble(3, prestamo.getMonto());
            sentencia.setDouble(4, prestamo.getInteres());
            sentencia.setInt(5, prestamo.getCuotas());
            sentencia.setDate(6, prestamo.getFechaInicio());
            sentencia.setDate(7, prestamo.getFechaVencimiento());
            sentencia.setDouble(8, prestamo.getMontoTotal());
            sentencia.setDouble(9, prestamo.getValorCuota());
            sentencia.setDouble(10, prestamo.getSaldoPendiente());
            sentencia.setString(11, prestamo.getEstado().name());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                // Recupera el identificador generado por MySQL.
                try (ResultSet claves = sentencia.getGeneratedKeys()) {

                    if (claves.next()) {
                        prestamo.setId(claves.getInt(1));
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
     * Consulta todos los préstamos registrados.
     *
     * Utilizamos INNER JOIN para recuperar también
     * los datos del cliente y del empleado asociados.
     */
    public ArrayList<Prestamo> listar() throws SQLException {

        ArrayList<Prestamo> listaPrestamos = new ArrayList<>();

        String sql = "SELECT "
                + "p.id AS prestamo_id, p.monto, p.interes, p.cuotas, "
                + "p.fecha_inicio, p.fecha_vencimiento, "
                + "p.monto_total, p.valor_cuota, "
                + "p.saldo_pendiente, p.estado, "
                + "c.id AS cliente_id, c.nombre AS cliente_nombre, "
                + "c.documento AS cliente_documento, "
                + "c.correo AS cliente_correo, "
                + "c.telefono AS cliente_telefono, "
                + "e.id AS empleado_id, e.nombre AS empleado_nombre, "
                + "e.documento AS empleado_documento, "
                + "e.correo AS empleado_correo, "
                + "e.rol AS empleado_rol, e.salario AS empleado_salario "
                + "FROM prestamos p "
                + "INNER JOIN clientes c ON p.cliente_id = c.id "
                + "INNER JOIN empleados e ON p.empleado_id = e.id "
                + "ORDER BY p.id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                // Convierte cada registro en un objeto Prestamo.
                Prestamo prestamo = crearPrestamo(resultado);

                listaPrestamos.add(prestamo);
            }
        }

        return listaPrestamos;
    }


    /*
     * MÉTODO LISTAR POR CLIENTE
     *
     * Consulta los prestamos asociados a un cliente especifico.
     */
    public ArrayList<Prestamo> listarPorCliente(int clienteId) throws SQLException {

        ArrayList<Prestamo> listaPrestamos = new ArrayList<>();

        String sql = "SELECT "
                + "p.id AS prestamo_id, p.monto, p.interes, p.cuotas, "
                + "p.fecha_inicio, p.fecha_vencimiento, "
                + "p.monto_total, p.valor_cuota, "
                + "p.saldo_pendiente, p.estado, "
                + "c.id AS cliente_id, c.nombre AS cliente_nombre, "
                + "c.documento AS cliente_documento, c.correo AS cliente_correo, "
                + "c.telefono AS cliente_telefono, "
                + "e.id AS empleado_id, e.nombre AS empleado_nombre, "
                + "e.documento AS empleado_documento, e.correo AS empleado_correo, "
                + "e.rol AS empleado_rol, e.salario AS empleado_salario "
                + "FROM prestamos p "
                + "INNER JOIN clientes c ON p.cliente_id = c.id "
                + "INNER JOIN empleados e ON p.empleado_id = e.id "
                + "WHERE p.cliente_id = ? ORDER BY p.id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, clienteId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    listaPrestamos.add(crearPrestamo(resultado));
                }
            }
        }

        return listaPrestamos;
    }


    /*
     * MÉTODO BUSCAR POR ID
     *
     * Busca un préstamo utilizando su identificador.
     *
     * Si lo encuentra, devuelve el objeto Prestamo.
     * Si no existe, devuelve null.
     */
    public Prestamo buscarPorId(int id) throws SQLException {

        String sql = "SELECT "
                + "p.id AS prestamo_id, p.monto, p.interes, p.cuotas, "
                + "p.fecha_inicio, p.fecha_vencimiento, "
                + "p.monto_total, p.valor_cuota, "
                + "p.saldo_pendiente, p.estado, "
                + "c.id AS cliente_id, c.nombre AS cliente_nombre, "
                + "c.documento AS cliente_documento, "
                + "c.correo AS cliente_correo, "
                + "c.telefono AS cliente_telefono, "
                + "e.id AS empleado_id, e.nombre AS empleado_nombre, "
                + "e.documento AS empleado_documento, "
                + "e.correo AS empleado_correo, "
                + "e.rol AS empleado_rol, e.salario AS empleado_salario "
                + "FROM prestamos p "
                + "INNER JOIN clientes c ON p.cliente_id = c.id "
                + "INNER JOIN empleados e ON p.empleado_id = e.id "
                + "WHERE p.id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return crearPrestamo(resultado);
                }
            }
        }

        return null;
    }


    /*
     * MÉTODO ACTUALIZAR
     *
     * Modifica la información de un préstamo existente.
     *
     * Se utiliza el ID para identificar el registro.
     */
    public boolean actualizar(Prestamo prestamo) throws SQLException {

        String sql = "UPDATE prestamos SET "
                + "cliente_id = ?, empleado_id = ?, monto = ?, "
                + "interes = ?, cuotas = ?, fecha_inicio = ?, "
                + "fecha_vencimiento = ?, monto_total = ?, "
                + "valor_cuota = ?, saldo_pendiente = ?, estado = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamo.getCliente().getId());
            sentencia.setInt(2, prestamo.getEmpleado().getId());
            sentencia.setDouble(3, prestamo.getMonto());
            sentencia.setDouble(4, prestamo.getInteres());
            sentencia.setInt(5, prestamo.getCuotas());
            sentencia.setDate(6, prestamo.getFechaInicio());
            sentencia.setDate(7, prestamo.getFechaVencimiento());
            sentencia.setDouble(8, prestamo.getMontoTotal());
            sentencia.setDouble(9, prestamo.getValorCuota());
            sentencia.setDouble(10, prestamo.getSaldoPendiente());
            sentencia.setString(11, prestamo.getEstado().name());
            sentencia.setInt(12, prestamo.getId());

            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * MÉTODO ELIMINAR
     *
     * Elimina un préstamo utilizando su identificador.
     *
     * MySQL puede impedir la eliminación si el préstamo
     * tiene pagos asociados mediante una clave foránea.
     */
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM prestamos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;
        }
    }


    /*
     * MÉTODO PRIVADO crearPrestamo()
     *
     * Convierte los datos obtenidos de MySQL en objetos:
     *
     * Cliente, Empleado y Prestamo.
     *
     * Permite reutilizar el mismo código en listar()
     * y buscarPorId().
     */
    private Prestamo crearPrestamo(ResultSet resultado)
            throws SQLException {

        Cliente cliente = new Cliente(
                resultado.getInt("cliente_id"),
                resultado.getString("cliente_nombre"),
                resultado.getString("cliente_documento"),
                resultado.getString("cliente_correo"),
                resultado.getString("cliente_telefono")
        );

        Empleado empleado = new Empleado(
                resultado.getInt("empleado_id"),
                resultado.getString("empleado_nombre"),
                resultado.getString("empleado_documento"),
                resultado.getString("empleado_correo"),
                resultado.getString("empleado_rol"),
                resultado.getDouble("empleado_salario")
        );

        Prestamo prestamo = new Prestamo(
                resultado.getInt("prestamo_id"),
                cliente,
                empleado,
                resultado.getDouble("monto"),
                resultado.getDouble("interes"),
                resultado.getInt("cuotas"),
                resultado.getDate("fecha_inicio"),
                resultado.getDate("fecha_vencimiento")
        );

        // Recupera el saldo y el estado almacenados en MySQL.
        prestamo.setSaldoPendiente(
                resultado.getDouble("saldo_pendiente")
        );

        prestamo.setEstado(
                EstadoPrestamo.valueOf(resultado.getString("estado"))
        );

        return prestamo;
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve PrestamoDAO?
     * Respuesta: Para realizar las operaciones de acceso
     * a la tabla prestamos de MySQL.
     *
     * 2. ¿Qué significa CRUD?
     * Respuesta: Crear, consultar, actualizar y eliminar datos.
     *
     * 3. ¿Para qué sirve INNER JOIN?
     * Respuesta: Para combinar información de varias tablas
     * que tienen una relación.
     *
     * 4. ¿Por qué relacionamos préstamos con clientes?
     * Respuesta: Porque cada préstamo pertenece a un cliente.
     *
     * 5. ¿Por qué relacionamos préstamos con empleados?
     * Respuesta: Para identificar al trabajador que gestiona
     * el préstamo.
     *
     * 6. ¿Para qué sirve PreparedStatement?
     * Respuesta: Para ejecutar consultas SQL utilizando
     * parámetros.
     *
     * 7. ¿Para qué sirve ResultSet?
     * Respuesta: Para recorrer y consultar los registros
     * obtenidos de una consulta SELECT.
     *
     * 8. ¿Para qué sirve ArrayList?
     * Respuesta: Para almacenar varios objetos Prestamo.
     *
     * 9. ¿Para qué sirve executeUpdate()?
     * Respuesta: Para ejecutar operaciones INSERT, UPDATE
     * y DELETE.
     *
     * 10. ¿Para qué sirve executeQuery()?
     * Respuesta: Para ejecutar consultas SELECT.
     *
     * 11. ¿Para qué sirve EstadoPrestamo?
     * Respuesta: Para identificar si el préstamo está
     * PENDIENTE o PAGADO.
     *
     * 12. ¿Para qué sirve crearPrestamo()?
     * Respuesta: Para convertir los datos de MySQL
     * en objetos Java.
     *
     * 13. ¿Por qué utilizamos claves foráneas?
     * Respuesta: Para mantener las relaciones entre
     * clientes, empleados, préstamos y pagos.
     *
     * =====================================================
     */
}