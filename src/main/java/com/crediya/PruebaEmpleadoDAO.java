package com.crediya;

import com.crediya.dao.EmpleadoDAO;
import com.crediya.model.Empleado;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE DE PRUEBAS: PruebaEmpleadoDAO
 *
 * Permite comprobar las operaciones CRUD de EmpleadoDAO
 * utilizando la base de datos MySQL.
 */
public class PruebaEmpleadoDAO {

    public static void main(String[] args) {

        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        try {

            // 1. INSERTAR EMPLEADO
            System.out.println("===== PRUEBA INSERTAR =====");

            Empleado empleado = new Empleado(
                    "Empleado de prueba",
                    "TEST-EMP-20261105",
                    "prueba.empleado@crediya.com",
                    "ASESOR",
                    2500000.00
            );

            if (empleadoDAO.insertar(empleado)) {
                System.out.println("Empleado registrado correctamente.");
                System.out.println("ID generado: " + empleado.getId());
            } else {
                System.out.println("No fue posible registrar el empleado.");
                return;
            }

            // 2. BUSCAR EMPLEADO
            System.out.println("\n===== PRUEBA BUSCAR =====");

            Empleado empleadoEncontrado =
                    empleadoDAO.buscarPorId(empleado.getId());

            if (empleadoEncontrado != null) {
                System.out.println(empleadoEncontrado);
            } else {
                System.out.println("No se encontró el empleado.");
            }

            // 3. ACTUALIZAR EMPLEADO
            System.out.println("\n===== PRUEBA ACTUALIZAR =====");

            empleado.setCorreo("actualizado.empleado@crediya.com");
            empleado.setRol("COORDINADOR");
            empleado.setSalario(2800000.00);

            if (empleadoDAO.actualizar(empleado)) {
                System.out.println("Empleado actualizado correctamente.");
            } else {
                System.out.println("No fue posible actualizar el empleado.");
            }

            // 4. LISTAR EMPLEADOS
            System.out.println("\n===== PRUEBA LISTAR =====");

            ArrayList<Empleado> listaEmpleados = empleadoDAO.listar();

            for (Empleado e : listaEmpleados) {
                System.out.println(e);
            }

            System.out.println("Total de empleados: "
                    + listaEmpleados.size());

            // 5. ELIMINAR EMPLEADO
            System.out.println("\n===== PRUEBA ELIMINAR =====");

            if (empleadoDAO.eliminar(empleado.getId())) {
                System.out.println("Empleado eliminado correctamente.");
            } else {
                System.out.println("No fue posible eliminar el empleado.");
            }

            // 6. VERIFICAR ELIMINACIÓN
            System.out.println("\n===== VERIFICAR ELIMINACIÓN =====");

            Empleado empleadoEliminado =
                    empleadoDAO.buscarPorId(empleado.getId());

            if (empleadoEliminado == null) {
                System.out.println("VERIFICACIÓN EXITOSA: "
                        + "El empleado ya no existe.");
            } else {
                System.out.println("El empleado todavía existe.");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL REALIZAR LAS OPERACIONES:");
            System.out.println(e.getMessage());
        }
    }
}