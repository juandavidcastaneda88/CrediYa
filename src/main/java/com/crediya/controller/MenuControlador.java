package com.crediya.controller;

import com.crediya.dao.ClienteDAO;
import com.crediya.dao.EmpleadoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.PagoDAO;
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;
import com.crediya.model.Pago;
import com.crediya.service.ArchivoService;
import com.crediya.service.ReporteService;
import com.crediya.view.MenuVista;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * CLASE: MenuControlador
 *
 * Coordina la vista con los DAO y los servicios del sistema.
 * Es el CONTROLADOR dentro de la arquitectura MVC.
 */
public class MenuControlador {

    private MenuVista vista = new MenuVista();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private PagoDAO pagoDAO = new PagoDAO();
    private ArchivoService archivoService = new ArchivoService();
    private ReporteService reporteService = new ReporteService();

    /* Inicia el menu principal y permite navegar por los modulos. */
    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenuPrincipal();
            opcion = vista.leerOpcion();

            switch (opcion) {
                case 1:
                    gestionarClientes();
                    break;
                case 2:
                    gestionarEmpleados();
                    break;
                case 3:
                    gestionarPrestamos();
                    break;
                case 4:
                    gestionarPagos();
                    break;
                case 5:
                    generarReportes();
                    break;
                case 6:
                    consultarReportes();
                    break;
                case 0:
                    vista.mostrarMensaje("Gracias por utilizar CrediYa S.A.S.");
                    break;
                default:
                    vista.mostrarMensaje("Opcion no valida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 0);
    }

    /* ======================== CLIENTES ======================== */

    private void gestionarClientes() {
        int opcion;
        do {
            vista.mostrarMenuClientes();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: registrarCliente(); break;
                case 2: buscarCliente(); break;
                case 3: listarClientes(); break;
                case 4: actualizarCliente(); break;
                case 5: eliminarCliente(); break;
                case 6: consultarPrestamosCliente(); break;
                case 0: vista.mostrarMensaje("Regresando al menu principal..."); break;
                default: vista.mostrarMensaje("Opcion no valida."); break;
            }
        } while (opcion != 0);
    }

    private void registrarCliente() {
        try {
            vista.mostrarMensaje("\n===== REGISTRAR CLIENTE =====");
            String nombre = vista.leerTexto("Nombre: ");
            String documento = vista.leerTexto("Documento: ");
            String correo = textoOpcional(vista.leerTexto("Correo (opcional): "));
            String telefono = textoOpcional(vista.leerTexto("Telefono (opcional): "));

            if (nombre.isBlank() || documento.isBlank()) {
                vista.mostrarMensaje("El nombre y el documento son obligatorios.");
                return;
            }

            Cliente cliente = new Cliente(nombre, documento, correo, telefono);
            if (clienteDAO.insertar(cliente)) {
                vista.mostrarMensaje("Cliente registrado. ID generado: " + cliente.getId());
                sincronizarReportes();
            } else {
                vista.mostrarMensaje("No fue posible registrar el cliente.");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error de MySQL al registrar el cliente: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        try {
            int id = leerId("Ingrese el ID del cliente: ");
            if (id <= 0) return;
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) vista.mostrarMensaje("No existe un cliente con ese ID.");
            else vista.mostrarMensaje(cliente.toString());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar el cliente: " + e.getMessage());
        }
    }

    private void listarClientes() {
        try {
            ArrayList<Cliente> clientes = clienteDAO.listar();
            if (clientes.isEmpty()) {
                vista.mostrarMensaje("No existen clientes registrados.");
                return;
            }
            for (Cliente cliente : clientes) {
                vista.mostrarMensaje(cliente.toString());
                vista.mostrarMensaje("----------------------------------------");
            }
            vista.mostrarMensaje("Total de clientes: " + clientes.size());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar clientes: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            int id = leerId("Ingrese el ID del cliente: ");
            if (id <= 0) return;
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                vista.mostrarMensaje("El cliente no existe.");
                return;
            }

            vista.mostrarMensaje("Datos actuales: " + cliente);
            String nombre = vista.leerTexto("Nuevo nombre: ");
            String documento = vista.leerTexto("Nuevo documento: ");
            String correo = textoOpcional(vista.leerTexto("Nuevo correo (opcional): "));
            String telefono = textoOpcional(vista.leerTexto("Nuevo telefono (opcional): "));

            if (nombre.isBlank() || documento.isBlank()) {
                vista.mostrarMensaje("El nombre y el documento son obligatorios.");
                return;
            }

            cliente.setNombre(nombre);
            cliente.setDocumento(documento);
            cliente.setCorreo(correo);
            cliente.setTelefono(telefono);

            if (clienteDAO.actualizar(cliente)) {
                vista.mostrarMensaje("Cliente actualizado correctamente.");
                sincronizarReportes();
            } else vista.mostrarMensaje("No se realizaron cambios.");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar el cliente: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            int id = leerId("Ingrese el ID del cliente: ");
            if (id <= 0) return;
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                vista.mostrarMensaje("El cliente no existe.");
                return;
            }
            vista.mostrarMensaje("Cliente: " + cliente.getNombre());
            String confirmacion = vista.leerTexto("Escriba SI para confirmar: ");
            if (confirmacion.equalsIgnoreCase("SI")) {
                if (clienteDAO.eliminar(id)) {
                    vista.mostrarMensaje("Cliente eliminado correctamente.");
                    sincronizarReportes();
                } else vista.mostrarMensaje("No fue posible eliminar el cliente.");
            } else vista.mostrarMensaje("Eliminacion cancelada.");
        } catch (SQLException e) {
            vista.mostrarMensaje("No se pudo eliminar. Verifique si tiene prestamos asociados. " + e.getMessage());
        }
    }

    private void consultarPrestamosCliente() {
        try {
            int id = leerId("Ingrese el ID del cliente: ");
            if (id <= 0) return;
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                vista.mostrarMensaje("El cliente no existe.");
                return;
            }
            ArrayList<Prestamo> prestamos = prestamoDAO.listarPorCliente(id);
            vista.mostrarMensaje("Prestamos de " + cliente.getNombre() + ":");
            mostrarPrestamos(prestamos);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al consultar prestamos del cliente: " + e.getMessage());
        }
    }

    /* ======================== EMPLEADOS ======================== */

    private void gestionarEmpleados() {
        int opcion;
        do {
            vista.mostrarMenuEmpleados();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: registrarEmpleado(); break;
                case 2: buscarEmpleado(); break;
                case 3: listarEmpleados(); break;
                case 4: actualizarEmpleado(); break;
                case 5: eliminarEmpleado(); break;
                case 0: vista.mostrarMensaje("Regresando al menu principal..."); break;
                default: vista.mostrarMensaje("Opcion no valida."); break;
            }
        } while (opcion != 0);
    }

    private void registrarEmpleado() {
        try {
            vista.mostrarMensaje("\n===== REGISTRAR EMPLEADO =====");
            String nombre = vista.leerTexto("Nombre: ");
            String documento = vista.leerTexto("Documento: ");
            String rol = vista.leerTexto("Rol: ");
            String correo = textoOpcional(vista.leerTexto("Correo (opcional): "));
            double salario = leerDouble("Salario: ");

            if (nombre.isBlank() || documento.isBlank() || rol.isBlank()) {
                vista.mostrarMensaje("Nombre, documento y rol son obligatorios.");
                return;
            }

            Empleado empleado = new Empleado(nombre, documento, correo, rol, salario);
            if (empleadoDAO.insertar(empleado)) {
                vista.mostrarMensaje("Empleado registrado. ID generado: " + empleado.getId());
                sincronizarReportes();
            } else vista.mostrarMensaje("No fue posible registrar el empleado.");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error de MySQL al registrar el empleado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void buscarEmpleado() {
        try {
            int id = leerId("Ingrese el ID del empleado: ");
            if (id <= 0) return;
            Empleado empleado = empleadoDAO.buscarPorId(id);
            if (empleado == null) vista.mostrarMensaje("No existe un empleado con ese ID.");
            else vista.mostrarMensaje(empleado.toString());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar empleado: " + e.getMessage());
        }
    }

    private void listarEmpleados() {
        try {
            ArrayList<Empleado> empleados = empleadoDAO.listar();
            if (empleados.isEmpty()) {
                vista.mostrarMensaje("No existen empleados registrados.");
                return;
            }
            for (Empleado empleado : empleados) {
                vista.mostrarMensaje(empleado.toString());
                vista.mostrarMensaje("----------------------------------------");
            }
            vista.mostrarMensaje("Total de empleados: " + empleados.size());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar empleados: " + e.getMessage());
        }
    }

    private void actualizarEmpleado() {
        try {
            int id = leerId("Ingrese el ID del empleado: ");
            if (id <= 0) return;
            Empleado empleado = empleadoDAO.buscarPorId(id);
            if (empleado == null) {
                vista.mostrarMensaje("El empleado no existe.");
                return;
            }
            vista.mostrarMensaje("Datos actuales: " + empleado);
            String nombre = vista.leerTexto("Nuevo nombre: ");
            String documento = vista.leerTexto("Nuevo documento: ");
            String rol = vista.leerTexto("Nuevo rol: ");
            String correo = textoOpcional(vista.leerTexto("Nuevo correo (opcional): "));
            double salario = leerDouble("Nuevo salario: ");

            if (nombre.isBlank() || documento.isBlank() || rol.isBlank()) {
                vista.mostrarMensaje("Nombre, documento y rol son obligatorios.");
                return;
            }
            empleado.setNombre(nombre);
            empleado.setDocumento(documento);
            empleado.setRol(rol);
            empleado.setCorreo(correo);
            empleado.setSalario(salario);

            if (empleadoDAO.actualizar(empleado)) {
                vista.mostrarMensaje("Empleado actualizado correctamente.");
                sincronizarReportes();
            } else vista.mostrarMensaje("No se realizaron cambios.");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar empleado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        try {
            int id = leerId("Ingrese el ID del empleado: ");
            if (id <= 0) return;
            Empleado empleado = empleadoDAO.buscarPorId(id);
            if (empleado == null) {
                vista.mostrarMensaje("El empleado no existe.");
                return;
            }
            vista.mostrarMensaje("Empleado: " + empleado.getNombre());
            String confirmacion = vista.leerTexto("Escriba SI para confirmar: ");
            if (confirmacion.equalsIgnoreCase("SI")) {
                if (empleadoDAO.eliminar(id)) {
                    vista.mostrarMensaje("Empleado eliminado correctamente.");
                    sincronizarReportes();
                } else vista.mostrarMensaje("No fue posible eliminar el empleado.");
            } else vista.mostrarMensaje("Eliminacion cancelada.");
        } catch (SQLException e) {
            vista.mostrarMensaje("No se pudo eliminar. Verifique si tiene prestamos asociados. " + e.getMessage());
        }
    }

    /* ======================== PRESTAMOS ======================== */

    private void gestionarPrestamos() {
        int opcion;
        do {
            vista.mostrarMenuPrestamos();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: registrarPrestamo(); break;
                case 2: buscarPrestamo(); break;
                case 3: listarPrestamos(); break;
                case 4: actualizarSaldoPrestamo(); break;
                case 5: eliminarPrestamo(); break;
                case 0: vista.mostrarMensaje("Regresando al menu principal..."); break;
                default: vista.mostrarMensaje("Opcion no valida."); break;
            }
        } while (opcion != 0);
    }

    private void registrarPrestamo() {
        try {
            vista.mostrarMensaje("\n===== REGISTRAR PRESTAMO =====");
            int clienteId = leerId("ID del cliente: ");
            if (clienteId <= 0) return;
            Cliente cliente = clienteDAO.buscarPorId(clienteId);
            if (cliente == null) {
                vista.mostrarMensaje("El cliente indicado no existe.");
                return;
            }

            int empleadoId = leerId("ID del empleado que gestiona el prestamo: ");
            if (empleadoId <= 0) return;
            Empleado empleado = empleadoDAO.buscarPorId(empleadoId);
            if (empleado == null) {
                vista.mostrarMensaje("El empleado indicado no existe.");
                return;
            }

            double monto = leerDouble("Monto del prestamo: ");
            double interes = leerDouble("Interes en porcentaje (ej. 10): ");
            int cuotas = leerId("Cantidad de cuotas mensuales: ");
            Date fechaInicio = leerFecha("Fecha de inicio (AAAA-MM-DD): ");
            Date fechaVencimiento = leerFecha("Fecha de vencimiento (AAAA-MM-DD): ");

            Prestamo prestamo = new Prestamo(cliente, empleado, monto, interes,
                    cuotas, fechaInicio, fechaVencimiento);

            if (prestamoDAO.insertar(prestamo)) {
                vista.mostrarMensaje("Prestamo registrado. ID generado: " + prestamo.getId());
                vista.mostrarMensaje("Monto total: " + prestamo.getMontoTotal());
                vista.mostrarMensaje("Valor de cuota: " + prestamo.getValorCuota());
                vista.mostrarMensaje("Saldo pendiente: " + prestamo.getSaldoPendiente());
                vista.mostrarMensaje("Estado: " + prestamo.getEstado());
                sincronizarReportes();
            } else vista.mostrarMensaje("No fue posible registrar el prestamo.");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error de MySQL al registrar el prestamo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void buscarPrestamo() {
        try {
            int id = leerId("Ingrese el ID del prestamo: ");
            if (id <= 0) return;
            Prestamo prestamo = prestamoDAO.buscarPorId(id);
            if (prestamo == null) vista.mostrarMensaje("El prestamo no existe.");
            else vista.mostrarMensaje(prestamo.toString());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar prestamo: " + e.getMessage());
        }
    }

    private void listarPrestamos() {
        try {
            mostrarPrestamos(prestamoDAO.listar());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar prestamos: " + e.getMessage());
        }
    }

    /* El DAO actualiza los datos del prestamo completo; en el menu se permite
       ajustar el saldo pendiente, respetando los limites definidos en el modelo. */
    private void actualizarSaldoPrestamo() {
        try {
            int id = leerId("Ingrese el ID del prestamo: ");
            if (id <= 0) return;
            Prestamo prestamo = prestamoDAO.buscarPorId(id);
            if (prestamo == null) {
                vista.mostrarMensaje("El prestamo no existe.");
                return;
            }
            vista.mostrarMensaje("Saldo actual: " + prestamo.getSaldoPendiente());
            double nuevoSaldo = leerDouble("Nuevo saldo pendiente (entre 0 y "
                    + prestamo.getMontoTotal() + "): ");
            prestamo.setSaldoPendiente(nuevoSaldo);
            if (prestamoDAO.actualizar(prestamo)) {
                vista.mostrarMensaje("Saldo actualizado. Estado: " + prestamo.getEstado());
                sincronizarReportes();
            } else vista.mostrarMensaje("No fue posible actualizar el prestamo.");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar prestamo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Saldo incorrecto: " + e.getMessage());
        }
    }

    private void eliminarPrestamo() {
        try {
            int id = leerId("Ingrese el ID del prestamo: ");
            if (id <= 0) return;
            Prestamo prestamo = prestamoDAO.buscarPorId(id);
            if (prestamo == null) {
                vista.mostrarMensaje("El prestamo no existe.");
                return;
            }
            vista.mostrarMensaje(prestamo.toString());
            String confirmacion = vista.leerTexto("Escriba SI para confirmar: ");
            if (confirmacion.equalsIgnoreCase("SI")) {
                if (prestamoDAO.eliminar(id)) {
                    vista.mostrarMensaje("Prestamo eliminado correctamente.");
                    sincronizarReportes();
                } else vista.mostrarMensaje("No fue posible eliminar el prestamo.");
            } else vista.mostrarMensaje("Eliminacion cancelada.");
        } catch (SQLException e) {
            vista.mostrarMensaje("No se pudo eliminar. El prestamo puede tener pagos asociados. " + e.getMessage());
        }
    }

    /* ======================== PAGOS ======================== */

    private void gestionarPagos() {
        int opcion;
        do {
            vista.mostrarMenuPagos();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: registrarPago(); break;
                case 2: buscarPago(); break;
                case 3: consultarHistorialPagos(); break;
                case 0: vista.mostrarMensaje("Regresando al menu principal..."); break;
                default: vista.mostrarMensaje("Opcion no valida."); break;
            }
        } while (opcion != 0);
    }

    private void registrarPago() {
        try {
            vista.mostrarMensaje("\n===== REGISTRAR PAGO =====");
            int prestamoId = leerId("ID del prestamo: ");
            if (prestamoId <= 0) return;
            Prestamo prestamo = prestamoDAO.buscarPorId(prestamoId);
            if (prestamo == null) {
                vista.mostrarMensaje("El prestamo no existe.");
                return;
            }
            vista.mostrarMensaje("Saldo pendiente: " + prestamo.getSaldoPendiente());
            Date fechaPago = leerFecha("Fecha del pago (AAAA-MM-DD): ");
            double monto = leerDouble("Monto del pago: ");

            Pago pago = new Pago(prestamo, fechaPago, monto);
            if (pagoDAO.registrarPago(pago)) {
                vista.mostrarMensaje("Pago registrado. ID generado: " + pago.getId());
                vista.mostrarMensaje("Nuevo saldo: " + prestamo.getSaldoPendiente());
                vista.mostrarMensaje("Estado del prestamo: " + prestamo.getEstado());
                sincronizarReportes();
            } else vista.mostrarMensaje("No fue posible registrar el pago.");
        } catch (SQLException e) {
            vista.mostrarMensaje("No se pudo registrar el pago: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos incorrectos: " + e.getMessage());
        }
    }

    private void buscarPago() {
        try {
            int id = leerId("Ingrese el ID del pago: ");
            if (id <= 0) return;
            Pago pago = pagoDAO.buscarPorId(id);
            if (pago == null) vista.mostrarMensaje("El pago no existe.");
            else vista.mostrarMensaje(pago.toString());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar pago: " + e.getMessage());
        }
    }

    private void consultarHistorialPagos() {
        try {
            int prestamoId = leerId("Ingrese el ID del prestamo: ");
            if (prestamoId <= 0) return;
            ArrayList<Pago> pagos = pagoDAO.listarPorPrestamo(prestamoId);
            if (pagos.isEmpty()) {
                vista.mostrarMensaje("El prestamo no tiene pagos registrados.");
                return;
            }
            for (Pago pago : pagos) {
                vista.mostrarMensaje(pago.toString());
                vista.mostrarMensaje("----------------------------------------");
            }
            vista.mostrarMensaje("Total de pagos: " + pagos.size());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al consultar historial: " + e.getMessage());
        }
    }

    /* ======================== REPORTES ======================== */

    private void generarReportes() {
        try {
            archivoService.generarReportes();
        } catch (SQLException e) {
            vista.mostrarMensaje("No se pudieron consultar los datos de MySQL: " + e.getMessage());
        } catch (IOException e) {
            vista.mostrarMensaje("No se pudieron escribir los archivos TXT: " + e.getMessage());
        }
    }

    private void consultarReportes() {
        int opcion;
        do {
            vista.mostrarMenuReportes();
            opcion = vista.leerOpcion();
            try {
                switch (opcion) {
                    case 1: generarReportes(); break;
                    case 2: archivoService.leerArchivo("clientes.txt"); break;
                    case 3: archivoService.leerArchivo("empleados.txt"); break;
                    case 4: archivoService.leerArchivo("prestamos.txt"); break;
                    case 5: archivoService.leerArchivo("pagos.txt"); break;
                    case 6: mostrarPrestamos(reporteService.listarPrestamosActivos()); break;
                    case 7: mostrarPrestamos(reporteService.listarPrestamosVencidos()); break;
                    case 8: mostrarClientesMorosos(); break;
                    case 0: vista.mostrarMensaje("Regresando al menu principal..."); break;
                    default: vista.mostrarMensaje("Opcion no valida."); break;
                }
            } catch (IOException e) {
                vista.mostrarMensaje("Error al leer el reporte: " + e.getMessage());
            } catch (SQLException e) {
                vista.mostrarMensaje("Error al consultar cartera: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarClientesMorosos() throws SQLException {
        ArrayList<Cliente> clientes = reporteService.listarClientesMorosos();
        if (clientes.isEmpty()) {
            vista.mostrarMensaje("No hay clientes morosos segun la definicion del sistema.");
            return;
        }
        vista.mostrarMensaje("CLIENTES MOROSOS:");
        for (Cliente cliente : clientes) {
            vista.mostrarMensaje(cliente.toString());
        }
        vista.mostrarMensaje("Total de clientes morosos: " + clientes.size());
        vista.mostrarMensaje("Definicion: tienen al menos un prestamo vencido con saldo pendiente.");
    }

    /* ======================== METODOS DE APOYO ======================== */

    private int leerId(String mensaje) {
        int id = vista.leerEntero(mensaje);
        if (id <= 0) vista.mostrarMensaje("Debe ingresar un ID entero mayor que cero.");
        return id;
    }

    private double leerDouble(String mensaje) {
        String entrada = vista.leerTexto(mensaje).replace(',', '.');
        try {
            double valor = Double.parseDouble(entrada);
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un numero valido.");
        }
    }

    private Date leerFecha(String mensaje) {
        String entrada = vista.leerTexto(mensaje);
        try {
            return Date.valueOf(entrada);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD.");
        }
    }

    private String textoOpcional(String texto) {
        return texto.isBlank() ? null : texto;
    }

    private void mostrarPrestamos(ArrayList<Prestamo> prestamos) {
        if (prestamos.isEmpty()) {
            vista.mostrarMensaje("No se encontraron prestamos.");
            return;
        }
        for (Prestamo prestamo : prestamos) {
            vista.mostrarMensaje(prestamo.toString());
            vista.mostrarMensaje("----------------------------------------");
        }
        vista.mostrarMensaje("Total de prestamos: " + prestamos.size());
    }

    /*
     * Actualiza los TXT despues de guardar cambios en MySQL.
     * MySQL sigue siendo la fuente principal de informacion.
     * Si falla el archivo, se informa sin afirmar que se actualizo.
     */
    private void sincronizarReportes() {
        try {
            archivoService.generarReportes();
        } catch (SQLException e) {
            vista.mostrarMensaje("AVISO: el cambio quedo guardado en MySQL, pero no se pudieron actualizar los reportes: " + e.getMessage());
        } catch (IOException e) {
            vista.mostrarMensaje("AVISO: el cambio quedo guardado en MySQL, pero no se pudieron escribir los archivos TXT: " + e.getMessage());
        }
    }

    /*
     * PREGUNTAS PARA EL PROFESOR
     *
     * 1. ¿Para que sirve MenuControlador?
     * Respuesta: Coordina las opciones de la vista con los DAO y servicios.
     *
     * 2. ¿Para que sirve switch?
     * Respuesta: Ejecuta una accion segun la opcion seleccionada.
     *
     * 3. ¿Por que se consultan cliente y empleado antes de crear un prestamo?
     * Respuesta: Para verificar que existan y cumplir las claves foraneas.
     *
     * 4. ¿Por que PagoDAO registra el pago?
     * Respuesta: Porque ese DAO ya controla la transaccion que inserta el pago
     * y actualiza el saldo y el estado del prestamo.
     *
     * 5. ¿Para que sirve sincronizarReportes()?
     * Respuesta: Para volver a consultar MySQL y actualizar los archivos TXT.
     */
}
