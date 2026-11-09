
package com.crediya.service;

import com.crediya.dao.ClienteDAO;
import com.crediya.dao.EmpleadoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.PagoDAO;

import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Prestamo;
import com.crediya.model.Pago;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE: ArchivoService
 *
 * Permite guardar y leer informacion mediante
 * archivos de texto dentro del sistema CrediYa.
 *
 * Consulta los datos de MySQL utilizando los DAO
 * y genera reportes de clientes, empleados,
 * prestamos y pagos.
 */
public class ArchivoService {

    // Objetos para consultar la informacion de MySQL.
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private PagoDAO pagoDAO = new PagoDAO();


    /*
     * METODO GENERAR REPORTES
     *
     * Crea la carpeta reportes si no existe.
     * Despues genera los cuatro archivos de texto.
     */
    public void generarReportes() throws SQLException, IOException {

        File carpeta = new File("reportes");

        if (!carpeta.exists()) {

            if (!carpeta.mkdirs()) {
                throw new IOException(
                        "No fue posible crear la carpeta reportes."
                );
            }
        }

        guardarClientes();
        guardarEmpleados();
        guardarPrestamos();
        guardarPagos();

        System.out.println("Reportes generados correctamente.");
    }


    /*
     * METODO GUARDAR CLIENTES
     *
     * Consulta los clientes de MySQL y guarda
     * su informacion en clientes.txt.
     */
    private void guardarClientes() throws SQLException, IOException {

        ArrayList<Cliente> listaClientes = clienteDAO.listar();

        try (BufferedWriter archivo = new BufferedWriter(
                new FileWriter("reportes/clientes.txt"))) {

            archivo.write("================================");
            archivo.newLine();
            archivo.write("       REPORTE DE CLIENTES");
            archivo.newLine();
            archivo.write("================================");
            archivo.newLine();
            archivo.newLine();

            for (Cliente cliente : listaClientes) {

                archivo.write(cliente.toString());
                archivo.newLine();
                archivo.newLine();
            }

            archivo.write("Total de clientes: "
                    + listaClientes.size());
        }
    }


    /*
     * METODO GUARDAR EMPLEADOS
     *
     * Consulta los empleados y guarda
     * su informacion en empleados.txt.
     */
    private void guardarEmpleados() throws SQLException, IOException {

        ArrayList<Empleado> listaEmpleados = empleadoDAO.listar();

        try (BufferedWriter archivo = new BufferedWriter(
                new FileWriter("reportes/empleados.txt"))) {

            archivo.write("================================");
            archivo.newLine();
            archivo.write("       REPORTE DE EMPLEADOS");
            archivo.newLine();
            archivo.write("================================");
            archivo.newLine();
            archivo.newLine();

            for (Empleado empleado : listaEmpleados) {

                archivo.write(empleado.toString());
                archivo.newLine();
                archivo.newLine();
            }

            archivo.write("Total de empleados: "
                    + listaEmpleados.size());
        }
    }


    /*
     * METODO GUARDAR PRESTAMOS
     *
     * Consulta los prestamos y guarda
     * su informacion en prestamos.txt.
     */
    private void guardarPrestamos() throws SQLException, IOException {

        ArrayList<Prestamo> listaPrestamos = prestamoDAO.listar();

        try (BufferedWriter archivo = new BufferedWriter(
                new FileWriter("reportes/prestamos.txt"))) {

            archivo.write("================================");
            archivo.newLine();
            archivo.write("       REPORTE DE PRESTAMOS");
            archivo.newLine();
            archivo.write("================================");
            archivo.newLine();
            archivo.newLine();

            for (Prestamo prestamo : listaPrestamos) {

                archivo.write(prestamo.toString());
                archivo.newLine();
                archivo.newLine();
            }

            archivo.write("Total de prestamos: "
                    + listaPrestamos.size());
        }
    }


    /*
     * METODO GUARDAR PAGOS
     *
     * Consulta los prestamos y sus pagos relacionados.
     * Guarda la informacion en pagos.txt.
     */
    private void guardarPagos() throws SQLException, IOException {

        ArrayList<Prestamo> listaPrestamos = prestamoDAO.listar();

        try (BufferedWriter archivo = new BufferedWriter(
                new FileWriter("reportes/pagos.txt"))) {

            archivo.write("================================");
            archivo.newLine();
            archivo.write("         REPORTE DE PAGOS");
            archivo.newLine();
            archivo.write("================================");
            archivo.newLine();
            archivo.newLine();

            int totalPagos = 0;

            for (Prestamo prestamo : listaPrestamos) {

                archivo.write("PRESTAMO ID: " + prestamo.getId());
                archivo.newLine();

                ArrayList<Pago> listaPagos =
                        pagoDAO.listarPorPrestamo(prestamo.getId());

                if (listaPagos.isEmpty()) {

                    archivo.write("No existen pagos registrados.");
                    archivo.newLine();

                } else {

                    for (Pago pago : listaPagos) {

                        archivo.write(pago.toString());
                        archivo.newLine();

                        totalPagos++;
                    }
                }

                archivo.newLine();
            }

            archivo.write("Total de pagos registrados: " + totalPagos);
        }
    }


    /*
     * METODO LEER ARCHIVO
     *
     * Abre un archivo de la carpeta reportes
     * y muestra su contenido en la consola.
     */
    public void leerArchivo(String nombreArchivo) throws IOException {

        File archivo = new File("reportes", nombreArchivo);

        if (!archivo.exists()) {
            throw new IOException(
                    "El archivo solicitado no existe."
            );
        }

        try (BufferedReader lector = new BufferedReader(
                new FileReader(archivo))) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                System.out.println(linea);
            }
        }
    }


    /*
     * =====================================================
     * PREGUNTAS
     * =====================================================
     *
     * 1. ¿Para que sirve ArchivoService?
     * Respuesta: Para guardar y leer informacion
     * mediante archivos de texto.
     *
     * 2. ¿Para que sirve File?
     * Respuesta: Para representar una ruta,
     * archivo o carpeta.
     *
     * 3. ¿Para que sirve FileWriter?
     * Respuesta: Para escribir informacion en un archivo.
     *
     * 4. ¿Para que sirve BufferedWriter?
     * Respuesta: Para escribir texto en un archivo.
     *
     * 5. ¿Para que sirve FileReader?
     * Respuesta: Para leer caracteres de un archivo.
     *
     * 6. ¿Para que sirve BufferedReader?
     * Respuesta: Para leer un archivo linea por linea.
     *
     * 7. ¿Para que sirve write()?
     * Respuesta: Para escribir texto en un archivo.
     *
     * 8. ¿Para que sirve readLine()?
     * Respuesta: Para leer una linea del archivo.
     *
     * 9. ¿Para que sirve newLine()?
     * Respuesta: Para agregar un salto de linea.
     *
     * 10. ¿Para que sirve exists()?
     * Respuesta: Para comprobar si un archivo o carpeta existe.
     *
     * 11. ¿Para que sirve mkdirs()?
     * Respuesta: Para crear una carpeta.
     *
     * 12. ¿Que significa IOException?
     * Respuesta: Es una excepcion que representa errores
     * durante operaciones de entrada y salida.
     *
     * 13. ¿Para que sirve ArrayList?
     * Respuesta: Para almacenar los objetos consultados
     * desde la base de datos.
     *
     * 14. ¿Por que utilizamos los DAO?
     * Respuesta: Para consultar los registros de MySQL
     * y utilizarlos al generar los reportes.
     *
     * =====================================================
     */
}
