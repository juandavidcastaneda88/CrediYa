package com.crediya.service;

import com.crediya.dao.ClienteDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.model.Cliente;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * CLASE: ReporteService
 *
 * Genera consultas de cartera utilizando expresiones Lambda
 * y Java Stream API, como se solicita en el proyecto.
 */
public class ReporteService {

    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();

    /* Devuelve los prestamos pendientes que tienen saldo por pagar. */
    public ArrayList<Prestamo> listarPrestamosActivos() throws SQLException {
        ArrayList<Prestamo> todos = prestamoDAO.listar();

        return new ArrayList<>(todos.stream()
                .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.PENDIENTE
                        && prestamo.getSaldoPendiente() > 0)
                .collect(Collectors.toList()));
    }

    /*
     * Un prestamo se considera vencido cuando su fecha general de
     * vencimiento ya paso y todavia tiene saldo pendiente.
     */
    public ArrayList<Prestamo> listarPrestamosVencidos() throws SQLException {
        ArrayList<Prestamo> todos = prestamoDAO.listar();
        LocalDate fechaActual = LocalDate.now();

        return new ArrayList<>(todos.stream()
                .filter(prestamo -> prestamo.getSaldoPendiente() > 0
                        && prestamo.getFechaVencimiento().toLocalDate()
                                .isBefore(fechaActual))
                .collect(Collectors.toList()));
    }

    /*
     * Un cliente se considera moroso si tiene al menos un prestamo
     * vencido con saldo pendiente. No se calcula mora por cuota porque
     * el esquema actual no tiene calendario de cuotas individuales.
     */
    public ArrayList<Cliente> listarClientesMorosos() throws SQLException {
        ArrayList<Cliente> clientes = clienteDAO.listar();
        ArrayList<Prestamo> prestamosVencidos = listarPrestamosVencidos();

        return new ArrayList<>(clientes.stream()
                .filter(cliente -> prestamosVencidos.stream()
                        .anyMatch(prestamo -> prestamo.getCliente().getId()
                                == cliente.getId()))
                .collect(Collectors.toList()));
    }

    /*
     * PREGUNTAS PARA EL PROFESOR
     *
     * 1. ¿Para que sirve Stream API?
     * Respuesta: Para recorrer y filtrar colecciones de forma sencilla.
     *
     * 2. ¿Para que sirve filter()?
     * Respuesta: Para conservar los elementos que cumplen una condicion.
     *
     * 3. ¿Que es una expresion Lambda?
     * Respuesta: Es una forma corta de escribir una funcion.
     *
     * 4. ¿Como se define un cliente moroso en este proyecto?
     * Respuesta: Tiene al menos un prestamo cuya fecha general de
     * vencimiento paso y conserva saldo pendiente.
     */
}
