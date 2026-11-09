
package com.crediya;

import com.crediya.controller.MenuControlador;

/*
 * CLASE: PruebaMenuControlador
 *
 * Permite iniciar el menu principal de CrediYa
 * para comprobar la integracion del controlador
 * con la vista y los DAO.
 */
public class PruebaMenuControlador {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("           CREDIYA S.A.S.");
        System.out.println("====================================");

        // Creamos el controlador del sistema.
        MenuControlador controlador = new MenuControlador();

        // Iniciamos el menu principal.
        controlador.iniciar();
    }

    /*
     * PREGUNTAS
     *
     * 1. ¿Para que sirve esta clase?
     * Respuesta: Para iniciar y probar el menu principal.
     *
     * 2. ¿Que hace MenuControlador?
     * Respuesta: Coordina las operaciones del sistema.
     *
     * 3. ¿Para que sirve main()?
     * Respuesta: Es el punto de entrada de la aplicacion Java.
     */
}
