
package com.crediya;

import com.crediya.dao.ClienteDAO;
import com.crediya.dao.EmpleadoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.PagoDAO;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Prestamo;
import com.crediya.model.Pago;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE DE PRUEBAS: PruebaPagoDAO
 *
 * Permite comprobar el registro y la consulta de pagos
 * utilizando la base de datos MySQL.
 *
 * También verifica que el saldo y el estado del préstamo
 * se actualicen automáticamente.
 *
 * IMPORTANTE:
 * Los registros de esta prueba permanecerán almacenados
 * en MySQL. No eliminamos el préstamo ni sus pagos.
 */
public class PruebaPagoDAO {

    public static void main(String[] args) {

        // Creamos los objetos para acceder a los DAO.
        ClienteDAO clienteDAO = new ClienteDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        PagoDAO pagoDAO = new PagoDAO();

        try {

            /*
             * 1. BUSCAR CLIENTE DE PRUEBA
             *
             * Utilizamos el cliente que registramos anteriormente.
             */
            System.out.println("===== BUSCAR CLIENTE =====");

            Cliente cliente = null;

            ArrayList<Cliente> listaClientes = clienteDAO.listar();

            for (Cliente c : listaClientes) {

                if (c.getDocumento().equals("CLI-PRESTAMO-001")) {
                    cliente = c;
                    break;
                }
            }

            if (cliente == null) {
                System.out.println("No se encontró el cliente de prueba.");
                return;
            }

            System.out.println("Cliente encontrado: "
                    + cliente.getNombre());

            System.out.println("ID: " + cliente.getId());


            /*
             * 2. BUSCAR EMPLEADO DE PRUEBA
             *
             * Utilizamos el empleado registrado anteriormente.
             */
            System.out.println("\n===== BUSCAR EMPLEADO =====");

            Empleado empleado = null;

            ArrayList<Empleado> listaEmpleados = empleadoDAO.listar();

            for (Empleado e : listaEmpleados) {

                if (e.getDocumento().equals("EMP-PRESTAMO-001")) {
                    empleado = e;
                    break;
                }
            }

            if (empleado == null) {
                System.out.println("No se encontró el empleado de prueba.");
                return;
            }

            System.out.println("Empleado encontrado: "
                    + empleado.getNombre());

            System.out.println("ID: " + empleado.getId());


            /*
             * 3. BUSCAR O CREAR PRÉSTAMO DE PRUEBA
             *
             * Buscamos si ya existe el préstamo de demostración.
             * Si no existe, lo registramos.
             *
             * Así evitamos crear préstamos duplicados
             * cuando ejecutemos nuevamente esta prueba.
             */
            System.out.println("\n===== BUSCAR PRÉSTAMO =====");

            Prestamo prestamo = null;

            ArrayList<Prestamo> listaPrestamos = prestamoDAO.listar();

            Date fechaInicio = Date.valueOf("2026-11-07");
            Date fechaVencimiento = Date.valueOf("2027-11-07");

            for (Prestamo p : listaPrestamos) {

                if (p.getCliente().getId() == cliente.getId()
                        && p.getEmpleado().getId() == empleado.getId()
                        && Math.abs(p.getMonto() - 1000000.00) < 0.01
                        && Math.abs(p.getInteres() - 10.0) < 0.001
                        && p.getCuotas() == 12
                        && p.getFechaInicio().equals(fechaInicio)
                        && p.getFechaVencimiento().equals(fechaVencimiento)) {

                    prestamo = p;
                    break;
                }
            }

            if (prestamo == null) {

                prestamo = new Prestamo(
                        cliente,
                        empleado,
                        1000000.00,
                        10.0,
                        12,
                        fechaInicio,
                        fechaVencimiento
                );

                if (prestamoDAO.insertar(prestamo)) {

                    System.out.println("Préstamo registrado correctamente.");
                    System.out.println("ID del préstamo: "
                            + prestamo.getId());

                } else {

                    System.out.println("No fue posible registrar el préstamo.");
                    return;
                }

            } else {

                System.out.println("Préstamo de prueba encontrado.");
                System.out.println("ID del préstamo: " + prestamo.getId());
            }


            /*
             * 4. CONSULTAR SALDO ACTUAL
             *
             * Consultamos nuevamente el préstamo desde MySQL
             * para trabajar con el saldo realmente almacenado.
             */
            prestamo = prestamoDAO.buscarPorId(prestamo.getId());

            if (prestamo == null) {
                System.out.println("No se encontró el préstamo.");
                return;
            }

            System.out.println("\n===== SALDO INICIAL =====");

            System.out.println("Saldo pendiente: "
                    + prestamo.getSaldoPendiente());

            System.out.println("Estado: " + prestamo.getEstado());


            /*
             * 5. REGISTRAR PRIMER PAGO
             *
             * Si el saldo corresponde al valor inicial del préstamo,
             * registramos un primer abono de 300.000.
             */
            System.out.println("\n===== PRIMER PAGO =====");

            if (Math.abs(prestamo.getSaldoPendiente() - 1100000.00) < 0.01) {

                Pago primerPago = new Pago(
                        prestamo,
                        Date.valueOf("2026-11-07"),
                        300000.00
                );

                if (pagoDAO.registrarPago(primerPago)) {

                    System.out.println("Primer pago registrado.");
                    System.out.println("ID del pago: "
                            + primerPago.getId());

                    System.out.println("Monto: "
                            + primerPago.getMonto());

                    // Consultamos el préstamo nuevamente en MySQL.
                    prestamo = prestamoDAO.buscarPorId(prestamo.getId());

                    System.out.println("Nuevo saldo: "
                            + prestamo.getSaldoPendiente());

                    System.out.println("Estado: "
                            + prestamo.getEstado());

                }

            } else {

                System.out.println(
                        "No se registra nuevamente el primer pago."
                );

                System.out.println(
                        "El préstamo ya tiene movimientos anteriores."
                );
            }


            /*
             * 6. REGISTRAR PAGO FINAL
             *
             * Si todavía existe saldo pendiente, pagamos
             * exactamente el valor restante.
             *
             * En una ejecución nueva, este pago será de 800.000.
             */
            System.out.println("\n===== PAGO FINAL =====");

            prestamo = prestamoDAO.buscarPorId(prestamo.getId());

            if (prestamo.getSaldoPendiente() > 0) {

                double montoFinal = prestamo.getSaldoPendiente();

                Pago segundoPago = new Pago(
                        prestamo,
                        Date.valueOf("2026-11-07"),
                        montoFinal
                );

                if (pagoDAO.registrarPago(segundoPago)) {

                    System.out.println("Pago final registrado.");
                    System.out.println("ID del pago: "
                            + segundoPago.getId());

                    System.out.println("Monto: "
                            + segundoPago.getMonto());
                }

            } else {

                System.out.println("El préstamo ya está completamente pagado.");
            }


            /*
             * 7. CONSULTAR ESTADO FINAL
             *
             * Recuperamos el préstamo desde MySQL para verificar
             * que el saldo y el estado quedaron guardados.
             */
            System.out.println("\n===== ESTADO FINAL DEL PRÉSTAMO =====");

            prestamo = prestamoDAO.buscarPorId(prestamo.getId());

            System.out.println("Saldo pendiente: "
                    + prestamo.getSaldoPendiente());

            System.out.println("Estado: " + prestamo.getEstado());


            /*
             * 8. LISTAR PAGOS
             *
             * Consultamos todos los pagos asociados
             * al préstamo de prueba.
             */
            System.out.println("\n===== HISTORIAL DE PAGOS =====");

            ArrayList<Pago> listaPagos =
                    pagoDAO.listarPorPrestamo(prestamo.getId());

            for (Pago p : listaPagos) {

                System.out.println(p);
                System.out.println("-------------------------");
            }

            System.out.println("Total de pagos registrados: "
                    + listaPagos.size());


            /*
             * 9. BUSCAR UN PAGO POR ID
             *
             * Consultamos el último pago registrado
             * utilizando su identificador.
             */
            System.out.println("\n===== BUSCAR PAGO =====");

            if (!listaPagos.isEmpty()) {

                Pago ultimoPago = listaPagos.get(listaPagos.size() - 1);

                Pago pagoEncontrado =
                        pagoDAO.buscarPorId(ultimoPago.getId());

                if (pagoEncontrado != null) {

                    System.out.println("Pago encontrado:");
                    System.out.println(pagoEncontrado);

                } else {

                    System.out.println("No se encontró el pago.");
                }
            }


            /*
             * 10. VERIFICAR RESULTADO
             *
             * Comprobamos que el préstamo haya quedado pagado
             * y que no tenga saldo pendiente.
             */
            System.out.println("\n===== VERIFICACIÓN FINAL =====");

            if (prestamo.getSaldoPendiente() == 0
                    && prestamo.getEstado().name().equals("PAGADO")) {

                System.out.println(
                        "VERIFICACIÓN EXITOSA: "
                        + "El préstamo está completamente PAGADO."
                );

            } else {

                System.out.println(
                        "El préstamo todavía tiene saldo pendiente."
                );
            }

            System.out.println("\nLOS DATOS PERMANECEN GUARDADOS EN MYSQL.");

        } catch (SQLException e) {

            System.out.println("ERROR AL REALIZAR LAS OPERACIONES:");
            System.out.println(e.getMessage());
        }
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve esta clase?
     * Respuesta: Para probar el registro y consulta de pagos
     * relacionados con un préstamo.
     *
     * 2. ¿Por qué buscamos primero el cliente y el empleado?
     * Respuesta: Porque el préstamo necesita estar relacionado
     * con registros que ya existen en MySQL.
     *
     * 3. ¿Para qué sirve registrarPago()?
     * Respuesta: Para guardar un pago y actualizar el saldo
     * y estado del préstamo.
     *
     * 4. ¿Qué sucede cuando el cliente realiza un pago?
     * Respuesta: Se registra el movimiento y disminuye
     * el saldo pendiente.
     *
     * 5. ¿Cuándo un préstamo queda PAGADO?
     * Respuesta: Cuando su saldo pendiente llega a cero.
     *
     * 6. ¿Para qué sirve listarPorPrestamo()?
     * Respuesta: Para consultar el historial de pagos
     * de un préstamo.
     *
     * 7. ¿Por qué los pagos permanecen en MySQL?
     * Respuesta: Porque representan movimientos económicos
     * y debemos conservar su historial.
     *
     * 8. ¿Qué diferencia existe entre el saldo y el monto
     * del préstamo?
     * Respuesta: El monto es el capital inicialmente solicitado;
     * el saldo es el dinero que todavía debe el cliente.
     *
     * =====================================================
     */
}
