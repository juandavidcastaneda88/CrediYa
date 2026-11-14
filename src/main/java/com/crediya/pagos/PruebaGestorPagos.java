package com.crediya.pagos;

import java.util.Scanner;

/**
 * Menú sencillo para probar el módulo y obtener las evidencias de consola.
 */
public class PruebaGestorPagos {

    public static void main(String[] args) {
        GestorPagos gestor = new GestorPagos();

        try (Scanner scanner = new Scanner(System.in)) {
            int opcion = -1;

            while (opcion != 0) {
                System.out.println("\n========== MÓDULO DE PAGOS CREDIYA ==========");
                System.out.println("1. Registrar pago");
                System.out.println("2. Mostrar historial de pagos");
                System.out.println("3. Mostrar pagos del último mes");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                try {
                    opcion = Integer.parseInt(scanner.nextLine().trim());

                    switch (opcion) {
                        case 1:
                            gestor.registrarPagoDesdeConsola(scanner);
                            break;
                        case 2:
                            System.out.print("ID del préstamo: ");
                            gestor.mostrarHistorialPagos(Integer.parseInt(scanner.nextLine().trim()));
                            break;
                        case 3:
                            System.out.print("ID del préstamo: ");
                            gestor.mostrarPagosUltimoMes(Integer.parseInt(scanner.nextLine().trim()));
                            break;
                        case 0:
                            System.out.println("Finalizando módulo de pagos.");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada no válida. Debe ingresar un número entero.");
                }
            }
        }
    }
}
