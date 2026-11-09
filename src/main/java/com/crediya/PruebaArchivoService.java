
package com.crediya;

import com.crediya.service.ArchivoService;

import java.io.IOException;
import java.sql.SQLException;

/*
 * CLASE DE PRUEBAS: PruebaArchivoService
 *
 * Permite comprobar que ArchivoService puede:
 *
 * 1. Generar los reportes de CrediYa.
 * 2. Guardar informacion en archivos de texto.
 * 3. Leer los archivos generados.
 *
 * Los datos se consultan desde MySQL mediante los DAO.
 */
public class PruebaArchivoService {

    public static void main(String[] args) {

        // Creamos el objeto para trabajar con los archivos.
        ArchivoService archivoService = new ArchivoService();

        try {

            /*
             * 1. GENERAR REPORTES
             *
             * Consulta MySQL y crea los cuatro archivos.
             */
            System.out.println("===== GENERAR REPORTES =====");

            archivoService.generarReportes();


            /*
             * 2. LEER REPORTE DE CLIENTES
             */
            System.out.println("\n===== REPORTE DE CLIENTES =====");

            archivoService.leerArchivo("clientes.txt");


            /*
             * 3. LEER REPORTE DE EMPLEADOS
             */
            System.out.println("\n===== REPORTE DE EMPLEADOS =====");

            archivoService.leerArchivo("empleados.txt");


            /*
             * 4. LEER REPORTE DE PRESTAMOS
             */
            System.out.println("\n===== REPORTE DE PRESTAMOS =====");

            archivoService.leerArchivo("prestamos.txt");


            /*
             * 5. LEER REPORTE DE PAGOS
             */
            System.out.println("\n===== REPORTE DE PAGOS =====");

            archivoService.leerArchivo("pagos.txt");


            /*
             * 6. FINALIZAR PRUEBA
             *
             * Si llegamos hasta aqui, los cuatro archivos
             * se generaron y pudieron leerse correctamente.
             */
            System.out.println("\n=================================");
            System.out.println("PRUEBA DE ARCHIVOS FINALIZADA");
            System.out.println("LOS CUATRO REPORTES FUERON LEIDOS.");
            System.out.println("=================================");

        } catch (SQLException e) {

            System.out.println("ERROR AL CONSULTAR MYSQL:");
            System.out.println(e.getMessage());

        } catch (IOException e) {

            System.out.println("ERROR AL TRABAJAR CON ARCHIVOS:");
            System.out.println(e.getMessage());
        }
    }


    /*
     * =====================================================
     * PREGUNTAS
     * =====================================================
     *
     * 1. ¿Para que sirve esta clase?
     * Respuesta: Para comprobar la generacion y lectura
     * de los reportes del sistema.
     *
     * 2. ¿Para que sirve generarReportes()?
     * Respuesta: Para crear los cuatro archivos de texto
     * con informacion consultada desde MySQL.
     *
     * 3. ¿Para que sirve leerArchivo()?
     * Respuesta: Para leer y mostrar el contenido
     * de un archivo de texto.
     *
     * 4. ¿Por que utilizamos ArchivoService?
     * Respuesta: Porque contiene los metodos encargados
     * de guardar y recuperar informacion mediante archivos.
     *
     * 5. ¿Por que utilizamos try y catch?
     * Respuesta: Para controlar los errores que pueden
     * ocurrir durante la ejecucion.
     *
     * 6. ¿Para que sirve SQLException?
     * Respuesta: Para controlar errores relacionados
     * con las operaciones de la base de datos.
     *
     * 7. ¿Para que sirve IOException?
     * Respuesta: Para controlar errores durante
     * la lectura y escritura de archivos.
     *
     * =====================================================
     */
}
