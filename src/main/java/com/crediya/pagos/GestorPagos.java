package com.crediya.pagos;

import com.crediya.pagos.excepciones.PrestamoNoEncontradoException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Gestiona el registro y la consulta de pagos asociados a préstamos.
 *
 * Suposición de esquema: la tabla prestamos contiene las columnas
 * id, saldo_pendiente y estado. La tabla pagos contiene id, prestamo_id,
 * monto y fecha_pago.
 */
public class GestorPagos {

    /**
     * Solicita los datos por consola y registra el pago.
     * La fecha debe escribirse en formato AAAA-MM-DD.
     */
    public void registrarPagoDesdeConsola(Scanner scanner) {
        try {
            System.out.println("\n--- REGISTRAR PAGO ---");
            System.out.print("ID del préstamo: ");
            int idPrestamo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Monto pagado: ");
            double monto = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));

            System.out.print("Fecha del pago (AAAA-MM-DD): ");
            LocalDate fechaPago = LocalDate.parse(scanner.nextLine().trim());

            registrarPago(idPrestamo, monto, fechaPago);
            System.out.println("Pago registrado correctamente.");

        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Error: la fecha no es válida. Usa el formato AAAA-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Error: el ID y el monto deben ser valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de base de datos al registrar el pago: " + e.getMessage());
        }
    }

    /**
     * Registra un pago y actualiza el saldo del préstamo dentro de una
     * transacción. Si el saldo llega a cero, el préstamo queda CANCELADO.
     */
    public void registrarPago(int idPrestamo, double monto, LocalDate fechaPago)
            throws PrestamoNoEncontradoException, SQLException {

        validarDatosPago(idPrestamo, monto, fechaPago);
        monto = redondearCentavos(monto);

        Connection conexion = null;

        try {
            conexion = ConexionPagos.obtenerConexion();
            conexion.setAutoCommit(false);

            double saldoPendiente;
            String estadoActual;

            String sqlPrestamo = "SELECT saldo_pendiente, estado FROM prestamos WHERE id = ? FOR UPDATE";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlPrestamo)) {
                sentencia.setInt(1, idPrestamo);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (!resultado.next()) {
                        throw new PrestamoNoEncontradoException(idPrestamo);
                    }
                    saldoPendiente = redondearCentavos(resultado.getDouble("saldo_pendiente"));
                    estadoActual = resultado.getString("estado");
                }
            }

            if ("CANCELADO".equalsIgnoreCase(estadoActual) || saldoPendiente <= 0) {
                throw new IllegalArgumentException("El préstamo ya está cancelado o no tiene saldo pendiente.");
            }

            if (monto > saldoPendiente) {
                throw new IllegalArgumentException(
                        String.format("El pago ($%.2f) supera el saldo pendiente ($%.2f).",
                                monto, saldoPendiente));
            }

            double nuevoSaldo = redondearCentavos(saldoPendiente - monto);
            if (nuevoSaldo < 0) {
                nuevoSaldo = 0;
            }

            String sqlPago = "INSERT INTO pagos (prestamo_id, monto, fecha_pago) VALUES (?, ?, ?)";
            try (PreparedStatement sentenciaPago = conexion.prepareStatement(sqlPago)) {
                sentenciaPago.setInt(1, idPrestamo);
                sentenciaPago.setDouble(2, monto);
                sentenciaPago.setDate(3, Date.valueOf(fechaPago));
                sentenciaPago.executeUpdate();
            }

            String nuevoEstado = nuevoSaldo == 0 ? "CANCELADO" : estadoActual;
            String sqlActualizar = "UPDATE prestamos SET saldo_pendiente = ?, estado = ? WHERE id = ?";
            try (PreparedStatement sentenciaActualizar = conexion.prepareStatement(sqlActualizar)) {
                sentenciaActualizar.setDouble(1, nuevoSaldo);
                sentenciaActualizar.setString(2, nuevoEstado);
                sentenciaActualizar.setInt(3, idPrestamo);

                int filasActualizadas = sentenciaActualizar.executeUpdate();
                if (filasActualizadas != 1) {
                    throw new SQLException("No se pudo actualizar el saldo del préstamo.");
                }
            }

            conexion.commit();
            System.out.printf("Saldo pendiente actualizado: $%.2f%n", nuevoSaldo);
            if (nuevoSaldo == 0) {
                System.out.println("El préstamo fue marcado como CANCELADO.");
            }

        } catch (PrestamoNoEncontradoException e) {
            hacerRollback(conexion, e);
            throw e;
        } catch (SQLException e) {
            hacerRollback(conexion, e);
            throw e;
        } catch (RuntimeException e) {
            hacerRollback(conexion, e);
            throw e;
        } finally {
            cerrarConexion(conexion);
        }
    }

    /**
     * Consulta todos los pagos de un préstamo en orden cronológico.
     */
    public List<PagoHistorial> consultarHistorialPagos(int idPrestamo)
            throws PrestamoNoEncontradoException, SQLException {

        Connection conexion = null;
        List<PagoHistorial> pagos = new ArrayList<>();

        try {
            conexion = ConexionPagos.obtenerConexion();
            verificarPrestamoExiste(conexion, idPrestamo);

            String sql = "SELECT id, prestamo_id, monto, fecha_pago "
                    + "FROM pagos WHERE prestamo_id = ? ORDER BY fecha_pago ASC, id ASC";

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idPrestamo);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    while (resultado.next()) {
                        pagos.add(new PagoHistorial(
                                resultado.getInt("id"),
                                resultado.getInt("prestamo_id"),
                                resultado.getDouble("monto"),
                                resultado.getDate("fecha_pago").toLocalDate()));
                    }
                }
            }

        } finally {
            cerrarConexion(conexion);
        }

        return pagos;
    }

    /** Muestra en consola el historial completo del préstamo. */
    public void mostrarHistorialPagos(int idPrestamo) {
        try {
            List<PagoHistorial> pagos = consultarHistorialPagos(idPrestamo);
            System.out.println("\n--- HISTORIAL DE PAGOS DEL PRÉSTAMO " + idPrestamo + " ---");
            mostrarListaPagos(pagos);
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de base de datos al consultar el historial: " + e.getMessage());
        }
    }

    /**
     * Filtra en memoria los pagos realizados entre dos fechas usando Stream API.
     * Los límites del rango se incluyen.
     */
    public List<PagoHistorial> consultarPagosEnRango(int idPrestamo, LocalDate fechaInicio,
            LocalDate fechaFin) throws PrestamoNoEncontradoException, SQLException {

        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }

        List<PagoHistorial> historial = consultarHistorialPagos(idPrestamo);

        return historial.stream()
                .filter(pago -> !pago.getFechaPago().isBefore(fechaInicio)
                        && !pago.getFechaPago().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    /** Muestra los pagos efectuados desde hace un mes hasta la fecha actual. */
    public void mostrarPagosUltimoMes(int idPrestamo) {
        LocalDate hoy = LocalDate.now();
        LocalDate haceUnMes = hoy.minusMonths(1);

        try {
            List<PagoHistorial> pagos = consultarPagosEnRango(idPrestamo, haceUnMes, hoy);
            System.out.println("\n--- PAGOS DEL ÚLTIMO MES (" + haceUnMes + " AL " + hoy + ") ---");
            mostrarListaPagos(pagos);
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de base de datos al filtrar los pagos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verificarPrestamoExiste(Connection conexion, int idPrestamo)
            throws SQLException, PrestamoNoEncontradoException {
        String sql = "SELECT id FROM prestamos WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idPrestamo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (!resultado.next()) {
                    throw new PrestamoNoEncontradoException(idPrestamo);
                }
            }
        }
    }

    private void mostrarListaPagos(List<PagoHistorial> pagos) {
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados para los criterios indicados.");
            return;
        }

        System.out.printf("%-8s %-13s %-16s %-12s%n", "ID", "PRÉSTAMO", "MONTO", "FECHA");
        for (PagoHistorial pago : pagos) {
            System.out.printf("%-8d %-13d $%-15.2f %-12s%n",
                    pago.getId(), pago.getPrestamoId(), pago.getMonto(), pago.getFechaPago());
        }
    }

    private void validarDatosPago(int idPrestamo, double monto, LocalDate fechaPago) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El ID del préstamo debe ser mayor que cero.");
        }
        if (Double.isNaN(monto) || Double.isInfinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser un número mayor que cero.");
        }
        if (fechaPago == null) {
            throw new IllegalArgumentException("La fecha del pago es obligatoria.");
        }
        if (Math.abs(monto - redondearCentavos(monto)) > 0.000001) {
            throw new IllegalArgumentException("El monto no puede tener más de dos decimales.");
        }
    }

    private double redondearCentavos(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private void hacerRollback(Connection conexion, Exception causa) {
        if (conexion != null) {
            try {
                conexion.rollback();
            } catch (SQLException e) {
                causa.addSuppressed(e);
            }
        }
    }

    /** Cierra la conexión de forma controlada desde el bloque finally. */
    private void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("No fue posible cerrar la conexión JDBC: " + e.getMessage());
            }
        }
    }

    /** Representa una fila del historial sin depender de la clase Pago existente del proyecto. */
    public static class PagoHistorial {

        private final int id;
        private final int prestamoId;
        private final double monto;
        private final LocalDate fechaPago;

        public PagoHistorial(int id, int prestamoId, double monto, LocalDate fechaPago) {
            this.id = id;
            this.prestamoId = prestamoId;
            this.monto = monto;
            this.fechaPago = fechaPago;
        }

        public int getId() {
            return id;
        }

        public int getPrestamoId() {
            return prestamoId;
        }

        public double getMonto() {
            return monto;
        }

        public LocalDate getFechaPago() {
            return fechaPago;
        }
    }
}
