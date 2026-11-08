
package com.crediya;

import com.crediya.dao.PagoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE DE PRUEBAS: PruebaValidacionesPagoDAO
 *
 * Comprueba que el sistema no permita:
 *
 * 1. Registrar pagos superiores al saldo pendiente.
 * 2. Crear pagos con monto igual a cero.
 * 3. Crear pagos con montos negativos.
 *
 * También verifica que las pruebas rechazadas
 * no modifiquen los registros de MySQL.
 */
public class PruebaValidacionesPagoDAO {

    public static void main(String[] args) {

        PagoDAO pagoDAO = new PagoDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        try {

            /*
             * 1. BUSCAR EL PRÉSTAMO DE PRUEBA
             *
             * Utilizamos el préstamo ID 2, que ya está pagado.
             * Su saldo pendiente es cero.
             */
            System.out.println("===== BUSCAR PRÉSTAMO =====");

            Prestamo prestamo = prestamoDAO.buscarPorId(2);

            if (prestamo == null) {
                System.out.println("No se encontró el préstamo.");
                return;
            }

            System.out.println("Préstamo encontrado: "
                    + prestamo.getId());

            System.out.println("Saldo pendiente: "
                    + prestamo.getSaldoPendiente());

            System.out.println("Estado: "
                    + prestamo.getEstado());


            /*
             * 2. GUARDAR EL ESTADO INICIAL
             *
             * Consultamos cuántos pagos existen antes
             * de realizar las pruebas.
             */
            double saldoInicial = prestamo.getSaldoPendiente();

            ArrayList<Pago> pagosIniciales =
                    pagoDAO.listarPorPrestamo(prestamo.getId());

            int cantidadInicial = pagosIniciales.size();


            /*
             * 3. PROBAR UN PAGO SUPERIOR AL SALDO
             *
             * Intentamos registrar un pago de $1.00.
             *
             * Como el préstamo tiene saldo cero,
             * el sistema debe rechazarlo.
             */
            System.out.println("\n===== PRUEBA PAGO SUPERIOR AL SALDO =====");

            try {

                Pago pagoExcedido = new Pago(
                        prestamo,
                        Date.valueOf("2026-11-07"),
                        1.00
                );

                pagoDAO.registrarPago(pagoExcedido);

                System.out.println(
                        "ERROR: El sistema aceptó un pago incorrecto."
                );

            } catch (SQLException e) {

                if (e.getMessage().contains(
                        "El pago supera el saldo pendiente.")) {

                    System.out.println(
                            "VALIDACIÓN CORRECTA: El pago fue rechazado."
                    );

                } else {

                    throw e;
                }
            }


            /*
             * 4. PROBAR UN PAGO IGUAL A CERO
             *
             * El constructor de Pago debe rechazar
             * este valor mediante una validación.
             */
            System.out.println("\n===== PRUEBA PAGO EN CERO =====");

            try {

                Pago pagoCero = new Pago(
                        prestamo,
                        Date.valueOf("2026-11-07"),
                        0.00
                );

                System.out.println("ERROR: Se aceptó un pago de cero.");

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "VALIDACIÓN CORRECTA: " + e.getMessage()
                );
            }


            /*
             * 5. PROBAR UN PAGO NEGATIVO
             *
             * El sistema no debe permitir montos negativos.
             */
            System.out.println("\n===== PRUEBA PAGO NEGATIVO =====");

            try {

                Pago pagoNegativo = new Pago(
                        prestamo,
                        Date.valueOf("2026-11-07"),
                        -100000.00
                );

                System.out.println("ERROR: Se aceptó un pago negativo.");

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "VALIDACIÓN CORRECTA: " + e.getMessage()
                );
            }


            /*
             * 6. VERIFICAR QUE MYSQL NO CAMBIÓ
             *
             * Volvemos a consultar el préstamo y sus pagos.
             *
             * Las pruebas rechazadas no deben insertar pagos
             * ni modificar el saldo.
             */
            System.out.println("\n===== VERIFICAR DATOS EN MYSQL =====");

            Prestamo prestamoFinal =
                    prestamoDAO.buscarPorId(prestamo.getId());

            ArrayList<Pago> pagosFinales =
                    pagoDAO.listarPorPrestamo(prestamo.getId());

            if (Math.abs(
                    saldoInicial - prestamoFinal.getSaldoPendiente()
            ) < 0.01
                    && cantidadInicial == pagosFinales.size()) {

                System.out.println(
                        "VERIFICACIÓN EXITOSA: Los datos no fueron modificados."
                );

                System.out.println("Saldo: "
                        + prestamoFinal.getSaldoPendiente());

                System.out.println("Pagos registrados: "
                        + pagosFinales.size());

            } else {

                System.out.println(
                        "ADVERTENCIA: Los datos cambiaron."
                );
            }

        } catch (SQLException e) {

            System.out.println("ERROR DE BASE DE DATOS:");
            System.out.println(e.getMessage());
        }
    }
}
