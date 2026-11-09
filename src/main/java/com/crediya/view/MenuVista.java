package com.crediya.view;

import java.util.Scanner;

/*
 * CLASE: MenuVista
 *
 * Representa la VISTA del sistema CrediYa.
 * Muestra los menus y recibe los datos del usuario.
 * No realiza consultas SQL; esa responsabilidad corresponde a los DAO.
 */
public class MenuVista {

    private Scanner teclado = new Scanner(System.in);

    public void mostrarMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("             CREDIYA S.A.S.");
        System.out.println("       SISTEMA DE GESTION DE CREDITOS");
        System.out.println("========================================");
        System.out.println("1. Gestion de clientes");
        System.out.println("2. Gestion de empleados");
        System.out.println("3. Gestion de prestamos");
        System.out.println("4. Gestion de pagos");
        System.out.println("5. Generar reportes TXT");
        System.out.println("6. Consultar reportes y cartera");
        System.out.println("0. Salir");
        System.out.println("========================================");
    }

    public void mostrarMenuClientes() {
        System.out.println("\n========================================");
        System.out.println("          GESTION DE CLIENTES");
        System.out.println("========================================");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Buscar cliente");
        System.out.println("3. Listar clientes");
        System.out.println("4. Actualizar cliente");
        System.out.println("5. Eliminar cliente");
        System.out.println("6. Consultar prestamos de un cliente");
        System.out.println("0. Volver al menu principal");
        System.out.println("========================================");
    }

    public void mostrarMenuEmpleados() {
        System.out.println("\n========================================");
        System.out.println("          GESTION DE EMPLEADOS");
        System.out.println("========================================");
        System.out.println("1. Registrar empleado");
        System.out.println("2. Buscar empleado");
        System.out.println("3. Listar empleados");
        System.out.println("4. Actualizar empleado");
        System.out.println("5. Eliminar empleado");
        System.out.println("0. Volver al menu principal");
        System.out.println("========================================");
    }

    public void mostrarMenuPrestamos() {
        System.out.println("\n========================================");
        System.out.println("          GESTION DE PRESTAMOS");
        System.out.println("========================================");
        System.out.println("1. Registrar prestamo");
        System.out.println("2. Buscar prestamo");
        System.out.println("3. Listar prestamos");
        System.out.println("4. Actualizar saldo pendiente");
        System.out.println("5. Eliminar prestamo");
        System.out.println("0. Volver al menu principal");
        System.out.println("========================================");
    }

    public void mostrarMenuPagos() {
        System.out.println("\n========================================");
        System.out.println("             GESTION DE PAGOS");
        System.out.println("========================================");
        System.out.println("1. Registrar pago");
        System.out.println("2. Buscar pago por ID");
        System.out.println("3. Consultar historial de un prestamo");
        System.out.println("0. Volver al menu principal");
        System.out.println("========================================");
    }

    public void mostrarMenuReportes() {
        System.out.println("\n========================================");
        System.out.println("          REPORTES DE CREDIYA");
        System.out.println("========================================");
        System.out.println("1. Generar todos los reportes TXT");
        System.out.println("2. Consultar reporte de clientes");
        System.out.println("3. Consultar reporte de empleados");
        System.out.println("4. Consultar reporte de prestamos");
        System.out.println("5. Consultar reporte de pagos");
        System.out.println("6. Consultar prestamos activos (Stream API)");
        System.out.println("7. Consultar prestamos vencidos (Stream API)");
        System.out.println("8. Consultar clientes morosos (Lambda y Stream API)");
        System.out.println("0. Volver al menu principal");
        System.out.println("========================================");
    }

    public int leerOpcion() {
        System.out.print("Seleccione una opcion: ");
        String entrada = teclado.nextLine();
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        String entrada = teclado.nextLine();
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /*
     * PREGUNTAS PARA EL PROFESOR
     *
     * 1. ¿Para que sirve MenuVista?
     * Respuesta: Para mostrar los menus y recibir datos del usuario.
     *
     * 2. ¿Que significa MVC?
     * Respuesta: Modelo, Vista y Controlador.
     *
     * 3. ¿Para que sirve Scanner?
     * Respuesta: Para leer los datos ingresados por teclado.
     *
     * 4. ¿Por que la vista no ejecuta SQL?
     * Respuesta: Porque las consultas a MySQL corresponden a los DAO.
     */
}
