package com.crediya;

import com.crediya.dao.ClienteDAO;
import com.crediya.model.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE DE PRUEBAS: PruebaClienteDAO
 *
 * Permite comprobar las operaciones CRUD de ClienteDAO
 * utilizando la base de datos MySQL.
 *
 * Esta clase es auxiliar y sirve para verificar
 * que las operaciones funcionan correctamente.
 */
public class PruebaClienteDAO {

    public static void main(String[] args) {

        // Creamos un objeto para acceder a las operaciones CRUD.
        ClienteDAO clienteDAO = new ClienteDAO();

        try {

            /*
             * 1. INSERTAR
             *
             * Creamos un cliente de prueba y lo registramos.
             */
            System.out.println("===== PRUEBA INSERTAR =====");

            Cliente cliente = new Cliente(
                    "Cliente de prueba",
                    "TEST-CLI-20261105",
                    "prueba@crediya.com",
                    "3001234567"
            );

            if (clienteDAO.insertar(cliente)) {

                System.out.println("Cliente registrado correctamente.");
                System.out.println("ID generado: " + cliente.getId());

            } else {

                System.out.println("No fue posible registrar el cliente.");
                return;
            }


            /*
             * 2. BUSCAR POR ID
             *
             * Consultamos el cliente que acabamos de registrar.
             */
            System.out.println("\n===== PRUEBA BUSCAR =====");

            Cliente clienteEncontrado =
                    clienteDAO.buscarPorId(cliente.getId());

            if (clienteEncontrado != null) {

                System.out.println(clienteEncontrado);

            } else {

                System.out.println("No se encontró el cliente.");
            }


            /*
             * 3. ACTUALIZAR
             *
             * Modificamos el correo y el teléfono del cliente.
             */
            System.out.println("\n===== PRUEBA ACTUALIZAR =====");

            cliente.setCorreo("actualizado@crediya.com");
            cliente.setTelefono("3109876543");

            if (clienteDAO.actualizar(cliente)) {

                System.out.println("Cliente actualizado correctamente.");

            } else {

                System.out.println("No fue posible actualizar el cliente.");
            }


            /*
             * 4. LISTAR
             *
             * Consultamos todos los clientes registrados.
             */
            System.out.println("\n===== PRUEBA LISTAR =====");

            ArrayList<Cliente> listaClientes = clienteDAO.listar();

            for (Cliente c : listaClientes) {
                System.out.println(c);
            }

            System.out.println("Total de clientes: "
                    + listaClientes.size());


            /*
             * 5. ELIMINAR
             *
             * Eliminamos el cliente de prueba utilizando su ID.
             */
            System.out.println("\n===== PRUEBA ELIMINAR =====");

            if (clienteDAO.eliminar(cliente.getId())) {

                System.out.println("Cliente eliminado correctamente.");

            } else {

                System.out.println("No fue posible eliminar el cliente.");
            }


            /*
             * 6. VERIFICAR ELIMINACIÓN
             *
             * Volvemos a buscar el cliente para comprobar
             * que ya no existe en la base de datos.
             */
            System.out.println("\n===== VERIFICAR ELIMINACIÓN =====");

            Cliente clienteEliminado =
                    clienteDAO.buscarPorId(cliente.getId());

            if (clienteEliminado == null) {

                System.out.println("VERIFICACIÓN EXITOSA: "
                        + "El cliente ya no existe.");

            } else {

                System.out.println("El cliente todavía existe.");
            }

        } catch (SQLException e) {

            System.out.println("ERROR AL REALIZAR LAS OPERACIONES:");
            System.out.println(e.getMessage());
        }
    }
}