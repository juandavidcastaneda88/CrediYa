
package com.crediya;

import com.crediya.database.ConexionDB;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrediYa {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("          CREDIYA S.A.S.");
        System.out.println("====================================");

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            DatabaseMetaData metadata = conexion.getMetaData();

            System.out.println("\nCONEXION EXITOSA");
            System.out.println("Base de datos: " + conexion.getCatalog());
            System.out.println("Servidor: "
                    + metadata.getDatabaseProductVersion());

            String sql = """
                    SELECT TABLE_NAME
                    FROM information_schema.TABLES
                    WHERE TABLE_SCHEMA = DATABASE()
                    AND TABLE_TYPE = 'BASE TABLE'
                    ORDER BY TABLE_NAME
                    """;

            try (Statement sentencia = conexion.createStatement();
                 ResultSet resultado = sentencia.executeQuery(sql)) {

                List<String> tablas = new ArrayList<>();

                while (resultado.next()) {
                    tablas.add(resultado.getString("TABLE_NAME"));
                }

                System.out.println("\nTABLAS ENCONTRADAS: "
                        + tablas.size());

                for (String tabla : tablas) {
                    System.out.println("- " + tabla);
                }

                Set<String> esperadas = Set.of(
                        "clientes",
                        "empleados",
                        "pagos",
                        "prestamos"
                );

                if (new HashSet<>(tablas).equals(esperadas)) {
                    System.out.println(
                            "\nESTRUCTURA DE BASE DE DATOS VERIFICADA"
                    );
                } else {
                    System.out.println(
                            "\nADVERTENCIA: Revisar las tablas existentes."
                    );
                }
            }

        } catch (SQLException | IllegalStateException e) {

            System.err.println("\nERROR EN EL SISTEMA");
            System.err.println(e.getMessage());
        }

        System.out.println("\n====================================");
    }
}
