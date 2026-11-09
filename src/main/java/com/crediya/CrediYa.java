package com.crediya;

import com.crediya.controller.MenuControlador;
import com.crediya.database.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * CLASE PRINCIPAL: CrediYa
 *
 * Es el punto de entrada de la aplicacion.
 * Comprueba la conexion con MySQL y despues inicia el menu.
 */
public class CrediYa {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("             CREDIYA S.A.S.");
        System.out.println("       SISTEMA DE COBROS DE CARTERA");
        System.out.println("========================================");

        // La conexion de comprobacion se cierra antes de abrir el menu.
        try (Connection conexion = ConexionDB.obtenerConexion()) {
            System.out.println("Conexion con MySQL exitosa.");
            System.out.println("Base de datos: " + conexion.getCatalog());
        } catch (SQLException | IllegalStateException e) {
            System.out.println("No fue posible iniciar CrediYa.");
            System.out.println("Revise MySQL y el archivo local db.properties.");
            System.out.println("Detalle: " + e.getMessage());
            return;
        }

        // Inicia la aplicacion una vez confirmada la conexion.
        MenuControlador controlador = new MenuControlador();
        controlador.iniciar();
    }
}
