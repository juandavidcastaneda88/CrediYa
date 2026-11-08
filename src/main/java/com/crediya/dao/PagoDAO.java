
package com.crediya.dao;

import com.crediya.database.ConexionDB;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE: PagoDAO
 *
 * DAO significa Data Access Object.
 *
 * Esta clase realiza las operaciones de acceso
 * a la tabla pagos de MySQL.
 *
 * Permite registrar pagos y consultar los pagos
 * realizados por los clientes.
 *
 * También actualiza el saldo y el estado del préstamo.
 */
public class PagoDAO {

    /*
     * MÉTODO REGISTRAR PAGO
     *
     * Registra un pago y actualiza el saldo del préstamo.
     *
     * Primero consulta el saldo actual.
     * Después verifica que el pago no supere ese saldo.
     * Finalmente registra el pago y actualiza el préstamo.
     *
     * Se utiliza una transacción para guardar ambas operaciones
     * juntas y evitar inconsistencias en la información.
     */
    public boolean registrarPago(Pago pago) throws SQLException {

        String sqlBuscar = "SELECT saldo_pendiente "
                + "FROM prestamos WHERE id = ? FOR UPDATE";

        String sqlInsertar = "INSERT INTO pagos "
                + "(prestamo_id, fecha_pago, monto) "
                + "VALUES (?, ?, ?)";

        String sqlActualizar = "UPDATE prestamos SET "
                + "saldo_pendiente = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            // Desactiva la confirmación automática de operaciones.
            conexion.setAutoCommit(false);

            try {

                double saldoActual;

                /*
                 * 1. CONSULTAR SALDO DEL PRÉSTAMO
                 *
                 * FOR UPDATE bloquea temporalmente el registro
                 * durante la transacción.
                 */
                try (PreparedStatement sentencia =
                        conexion.prepareStatement(sqlBuscar)) {

                    sentencia.setInt(
                            1, pago.getPrestamo().getId()
                    );

                    try (ResultSet resultado =
                            sentencia.executeQuery()) {

                        if (!resultado.next()) {
                            throw new SQLException(
                                    "No se encontró el préstamo."
                            );
                        }

                        saldoActual =
                                resultado.getDouble("saldo_pendiente");
                    }
                }

                /*
                 * 2. VALIDAR EL MONTO
                 *
                 * El pago debe ser mayor que cero y no puede
                 * superar el saldo pendiente del préstamo.
                 */
                if (pago.getMonto() <= 0) {
                    throw new SQLException(
                            "El monto del pago debe ser mayor que cero."
                    );
                }

                if (pago.getMonto() > saldoActual) {
                    throw new SQLException(
                            "El pago supera el saldo pendiente."
                    );
                }

                /*
                 * 3. CALCULAR EL NUEVO SALDO
                 *
                 * Restamos el pago al saldo actual.
                 * Redondeamos el resultado a dos decimales.
                 */
                double nuevoSaldo = saldoActual - pago.getMonto();

                nuevoSaldo =
                        Math.round(nuevoSaldo * 100.0) / 100.0;

                /*
                 * 4. DETERMINAR EL ESTADO
                 *
                 * Si el saldo llega a cero, queda PAGADO.
                 * Si todavía existe una deuda, queda PENDIENTE.
                 */
                String nuevoEstado;

                if (nuevoSaldo == 0) {
                    nuevoEstado = "PAGADO";
                } else {
                    nuevoEstado = "PENDIENTE";
                }

                /*
                 * 5. REGISTRAR EL PAGO
                 *
                 * Insertamos el movimiento en la tabla pagos.
                 */
                int idPago = 0;

                try (PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sqlInsertar,
                                Statement.RETURN_GENERATED_KEYS)) {

                    sentencia.setInt(
                            1, pago.getPrestamo().getId()
                    );

                    sentencia.setDate(2, pago.getFechaPago());
                    sentencia.setDouble(3, pago.getMonto());

                    int filasAfectadas =
                            sentencia.executeUpdate();

                    if (filasAfectadas == 0) {
                        throw new SQLException(
                                "No fue posible registrar el pago."
                        );
                    }

                    // Recupera el ID generado por MySQL.
                    try (ResultSet claves =
                            sentencia.getGeneratedKeys()) {

                        if (claves.next()) {
                            idPago = claves.getInt(1);
                        }
                    }
                }

                /*
                 * 6. ACTUALIZAR EL PRÉSTAMO
                 *
                 * Guardamos el nuevo saldo y el estado actualizado.
                 */
                try (PreparedStatement sentencia =
                        conexion.prepareStatement(sqlActualizar)) {

                    sentencia.setDouble(1, nuevoSaldo);
                    sentencia.setString(2, nuevoEstado);
                    sentencia.setInt(
                            3, pago.getPrestamo().getId()
                    );

                    if (sentencia.executeUpdate() == 0) {
                        throw new SQLException(
                                "No fue posible actualizar el préstamo."
                        );
                    }
                }

                /*
                 * 7. CONFIRMAR TRANSACCIÓN
                 *
                 * Guarda definitivamente las dos operaciones
                 * en MySQL.
                 */
                conexion.commit();

                // Actualiza el objeto Java después de confirmar.
                pago.setId(idPago);

                pago.getPrestamo().setSaldoPendiente(nuevoSaldo);

                pago.getPrestamo().setEstado(
                        EstadoPrestamo.valueOf(nuevoEstado)
                );

                return true;

            } catch (SQLException | RuntimeException e) {

                // Cancela las operaciones si ocurre algún error.
                conexion.rollback();

                throw e;
            }
        }
    }


    /*
     * MÉTODO LISTAR POR PRÉSTAMO
     *
     * Consulta todos los pagos asociados a un préstamo.
     *
     * ArrayList almacena los objetos Pago encontrados.
     */
    public ArrayList<Pago> listarPorPrestamo(int prestamoId)
            throws SQLException {

        ArrayList<Pago> listaPagos = new ArrayList<>();

        // Consultamos el préstamo relacionado.
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        Prestamo prestamo = prestamoDAO.buscarPorId(prestamoId);

        if (prestamo == null) {
            throw new SQLException(
                    "No existe el préstamo consultado."
            );
        }

        String sql = "SELECT id, fecha_pago, monto "
                + "FROM pagos WHERE prestamo_id = ? "
                + "ORDER BY fecha_pago, id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamoId);

            try (ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {

                    Pago pago = new Pago(
                            resultado.getInt("id"),
                            prestamo,
                            resultado.getDate("fecha_pago"),
                            resultado.getDouble("monto")
                    );

                    listaPagos.add(pago);
                }
            }
        }

        return listaPagos;
    }


    /*
     * MÉTODO BUSCAR POR ID
     *
     * Busca un pago utilizando su identificador.
     *
     * Si lo encuentra, devuelve el objeto Pago.
     * Si no existe, devuelve null.
     */
    public Pago buscarPorId(int id) throws SQLException {

        String sql = "SELECT prestamo_id, fecha_pago, monto "
                + "FROM pagos WHERE id = ?";

        int prestamoId;
        java.sql.Date fechaPago;
        double monto;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (!resultado.next()) {
                    return null;
                }

                prestamoId = resultado.getInt("prestamo_id");
                fechaPago = resultado.getDate("fecha_pago");
                monto = resultado.getDouble("monto");
            }
        }

        // Consultamos el préstamo relacionado con el pago.
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        Prestamo prestamo = prestamoDAO.buscarPorId(prestamoId);

        if (prestamo == null) {
            return null;
        }

        return new Pago(
                id,
                prestamo,
                fechaPago,
                monto
        );
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve PagoDAO?
     * Respuesta: Para registrar y consultar los pagos
     * almacenados en MySQL.
     *
     * 2. ¿Para qué sirve registrarPago()?
     * Respuesta: Para registrar un pago y actualizar
     * automáticamente el saldo del préstamo.
     *
     * 3. ¿Para qué sirve setAutoCommit(false)?
     * Respuesta: Para controlar manualmente cuándo
     * se confirman las operaciones en la base de datos.
     *
     * 4. ¿Para qué sirve commit()?
     * Respuesta: Para confirmar y guardar las operaciones.
     *
     * 5. ¿Para qué sirve rollback()?
     * Respuesta: Para cancelar las operaciones cuando
     * ocurre un error durante la transacción.
     *
     * 6. ¿Por qué verificamos el saldo pendiente?
     * Respuesta: Para evitar que el cliente pague más
     * dinero del que realmente debe.
     *
     * 7. ¿Cómo se actualiza el estado del préstamo?
     * Respuesta: Si el saldo llega a cero queda PAGADO;
     * si todavía existe deuda queda PENDIENTE.
     *
     * 8. ¿Para qué sirve PreparedStatement?
     * Respuesta: Para ejecutar consultas SQL utilizando
     * parámetros.
     *
     * 9. ¿Para qué sirve ResultSet?
     * Respuesta: Para recorrer los resultados de consultas
     * realizadas a MySQL.
     *
     * 10. ¿Para qué sirve ArrayList?
     * Respuesta: Para almacenar varios objetos Pago.
     *
     * 11. ¿Para qué sirve listarPorPrestamo()?
     * Respuesta: Para consultar todos los pagos asociados
     * a un préstamo específico.
     *
     * 12. ¿Para qué sirve buscarPorId()?
     * Respuesta: Para consultar un pago mediante
     * su identificador.
     *
     * 13. ¿Por qué utilizamos EstadoPrestamo?
     * Respuesta: Para identificar si el préstamo está
     * pendiente o pagado.
     *
     * 14. ¿Por qué conservamos los pagos?
     * Respuesta: Porque representan movimientos económicos
     * y permiten mantener el historial de los abonos.
     *
     * =====================================================
     */
}
