package com.crediya;

import com.crediya.dao.ClienteDAO;
import com.crediya.dao.EmpleadoDAO;
import com.crediya.dao.PrestamoDAO;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Prestamo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE DE PRUEBAS: PruebaPrestamoDAO
 *
 * Permite comprobar las operaciones de PrestamoDAO
 * utilizando la base de datos MySQL.
 *
 * También prepara un cliente y un empleado de prueba
 * para relacionarlos con el préstamo.
 */
public class PruebaPrestamoDAO {

    public static void main(String[] args) {

        // Creamos los objetos para acceder a los DAO.
        ClienteDAO clienteDAO = new ClienteDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        try {

            /*
             * 1. BUSCAR O CREAR CLIENTE DE PRUEBA
             *
             * Primero consultamos si el cliente ya existe.
             * Si no existe, lo registramos en MySQL.
             *
             * De esta manera evitamos duplicar su documento
             * cuando ejecutemos nuevamente esta prueba.
             */
            System.out.println("===== CLIENTE DE PRUEBA =====");

            Cliente cliente = null;

            ArrayList<Cliente> listaClientes = clienteDAO.listar();

            for (Cliente c : listaClientes) {

                if (c.getDocumento().equals("CLI-PRESTAMO-001")) {
                    cliente = c;
                    break;
                }
            }

            if (cliente == null) {

                cliente = new Cliente(
                        "Cliente de prueba préstamo",
                        "CLI-PRESTAMO-001",
                        "cliente.prestamo@crediya.com",
                        "3001234567"
                );

                if (clienteDAO.insertar(cliente)) {
                    System.out.println("Cliente registrado correctamente.");
                } else {
                    System.out.println("No fue posible registrar el cliente.");
                    return;
                }

            } else {

                System.out.println("Cliente de prueba encontrado.");
            }

            System.out.println("ID del cliente: " + cliente.getId());


            /*
             * 2. BUSCAR O CREAR EMPLEADO DE PRUEBA
             *
             * Consultamos si el empleado ya está registrado.
             * Si no existe, lo insertamos en MySQL.
             *
             * El empleado permanecerá guardado para futuras pruebas.
             */
            System.out.println("\n===== EMPLEADO DE PRUEBA =====");

            Empleado empleado = null;

            ArrayList<Empleado> listaEmpleados = empleadoDAO.listar();

            for (Empleado e : listaEmpleados) {

                if (e.getDocumento().equals("EMP-PRESTAMO-001")) {
                    empleado = e;
                    break;
                }
            }

            if (empleado == null) {

                empleado = new Empleado(
                        "Empleado de prueba préstamo",
                        "EMP-PRESTAMO-001",
                        "empleado.prestamo@crediya.com",
                        "ASESOR",
                        2500000.00
                );

                if (empleadoDAO.insertar(empleado)) {
                    System.out.println("Empleado registrado correctamente.");
                } else {
                    System.out.println("No fue posible registrar el empleado.");
                    return;
                }

            } else {

                System.out.println("Empleado de prueba encontrado.");
            }

            System.out.println("ID del empleado: " + empleado.getId());


            /*
             * 3. INSERTAR PRÉSTAMO
             *
             * Creamos un préstamo relacionado con el cliente
             * y el empleado que acabamos de consultar o registrar.
             */
            System.out.println("\n===== PRUEBA INSERTAR PRÉSTAMO =====");

            Prestamo prestamo = new Prestamo(
                    cliente,
                    empleado,
                    1000000.00,
                    10.0,
                    12,
                    Date.valueOf("2026-11-06"),
                    Date.valueOf("2027-11-06")
            );

            if (prestamoDAO.insertar(prestamo)) {

                System.out.println("Préstamo registrado correctamente.");
                System.out.println("ID generado: " + prestamo.getId());
                System.out.println(prestamo);

            } else {

                System.out.println("No fue posible registrar el préstamo.");
                return;
            }


            /*
             * 4. BUSCAR PRÉSTAMO
             *
             * Consultamos el préstamo utilizando su identificador.
             */
            System.out.println("\n===== PRUEBA BUSCAR PRÉSTAMO =====");

            Prestamo prestamoEncontrado =
                    prestamoDAO.buscarPorId(prestamo.getId());

            if (prestamoEncontrado != null) {

                System.out.println(prestamoEncontrado);

            } else {

                System.out.println("No se encontró el préstamo.");
            }


            /*
             * 5. LISTAR PRÉSTAMOS
             *
             * Consultamos todos los préstamos registrados.
             */
            System.out.println("\n===== PRUEBA LISTAR PRÉSTAMOS =====");

            ArrayList<Prestamo> listaPrestamos = prestamoDAO.listar();

            for (Prestamo p : listaPrestamos) {
                System.out.println(p);
                System.out.println("---------------------------");
            }

            System.out.println("Total de préstamos: "
                    + listaPrestamos.size());


            /*
             * 6. ACTUALIZAR PRÉSTAMO
             *
             * Simulamos una disminución del saldo pendiente.
             *
             * No estamos registrando todavía un pago formal.
             * Esa operación la implementaremos con PagoDAO.
             */
            System.out.println("\n===== PRUEBA ACTUALIZAR PRÉSTAMO =====");

            double nuevoSaldo =
                    prestamoEncontrado.getSaldoPendiente() - 100000.00;

            prestamoEncontrado.setSaldoPendiente(nuevoSaldo);

            if (prestamoDAO.actualizar(prestamoEncontrado)) {

                System.out.println("Préstamo actualizado correctamente.");

                System.out.println("Nuevo saldo: "
                        + prestamoEncontrado.getSaldoPendiente());

                System.out.println("Estado: "
                        + prestamoEncontrado.getEstado());

            } else {

                System.out.println("No fue posible actualizar el préstamo.");
            }


            /*
             * 7. VERIFICAR ACTUALIZACIÓN
             *
             * Consultamos nuevamente el préstamo para comprobar
             * que el nuevo saldo quedó guardado en MySQL.
             */
            System.out.println("\n===== VERIFICAR ACTUALIZACIÓN =====");

            Prestamo prestamoActualizado =
                    prestamoDAO.buscarPorId(prestamo.getId());

            if (prestamoActualizado != null) {

                System.out.println("Nuevo saldo guardado: "
                        + prestamoActualizado.getSaldoPendiente());

                System.out.println("Actualización verificada.");

            }


            /*
             * 8. ELIMINAR PRÉSTAMO
             *
             * Eliminamos el préstamo de prueba.
             *
             * Los clientes y empleados de prueba NO se eliminan.
             */
            System.out.println("\n===== PRUEBA ELIMINAR PRÉSTAMO =====");

            if (prestamoDAO.eliminar(prestamo.getId())) {

                System.out.println("Préstamo eliminado correctamente.");

            } else {

                System.out.println("No fue posible eliminar el préstamo.");
            }


            /*
             * 9. VERIFICAR ELIMINACIÓN
             *
             * Comprobamos que el préstamo ya no exista en MySQL.
             */
            System.out.println("\n===== VERIFICAR ELIMINACIÓN =====");

            Prestamo prestamoEliminado =
                    prestamoDAO.buscarPorId(prestamo.getId());

            if (prestamoEliminado == null) {

                System.out.println("VERIFICACIÓN EXITOSA: "
                        + "El préstamo ya no existe.");

            } else {

                System.out.println("El préstamo todavía existe.");
            }

        } catch (SQLException e) {

            System.out.println("ERROR AL REALIZAR LAS OPERACIONES:");
            System.out.println(e.getMessage());
        }
    }
}