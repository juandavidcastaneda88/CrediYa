package com.crediya.model;

import java.sql.Date;

/*
 * CLASE: Prestamo
 *
 * Representa un préstamo realizado por CrediYa S.A.S.
 *
 * Esta clase permite almacenar la información del préstamo,
 * relacionarlo con un cliente y un empleado, calcular sus valores
 * y controlar su estado.
 */
public class Prestamo {

    // Atributos privados: se utiliza encapsulamiento para proteger
    // la información y controlar el acceso mediante métodos.
    private int id;

    // Cliente que recibe el préstamo.
    private Cliente cliente;

    // Empleado que gestiona el préstamo.
    private Empleado empleado;

    private double monto;
    private double interes;
    private int cuotas;

    // Fechas del préstamo.
    private Date fechaInicio;
    private Date fechaVencimiento;

    // Valores calculados a partir del monto y el interés.
    private double montoTotal;
    private double valorCuota;
    private double saldoPendiente;

    // Estado actual del préstamo: PENDIENTE o PAGADO.
    private EstadoPrestamo estado;


    /*
     * CONSTRUCTOR
     *
     * Se utiliza cuando se crea un préstamo nuevo.
     *
     * El valor 0 indica que todavía no tiene un identificador
     * asignado por la base de datos.
     *
     * this(...) permite llamar al otro constructor de esta clase.
     */
    public Prestamo(Cliente cliente, Empleado empleado,
                    double monto, double interes, int cuotas,
                    Date fechaInicio, Date fechaVencimiento) {

        this(0, cliente, empleado, monto, interes, cuotas,
                fechaInicio, fechaVencimiento);
    }


    /*
     * SEGUNDO CONSTRUCTOR
     *
     * Se utiliza cuando el préstamo ya tiene un identificador,
     * por ejemplo, cuando se consulta desde la base de datos.
     */
    public Prestamo(int id, Cliente cliente, Empleado empleado,
                    double monto, double interes, int cuotas,
                    Date fechaInicio, Date fechaVencimiento) {

        // Verifica que el préstamo tenga un cliente y un empleado.
        if (cliente == null || empleado == null) {
            throw new IllegalArgumentException(
                    "El préstamo debe tener un cliente y un empleado."
            );
        }

        // Valida que los valores del préstamo sean correctos.
        if (monto <= 0 || interes < 0 || cuotas <= 0) {
            throw new IllegalArgumentException(
                    "Los valores del préstamo no son válidos."
            );
        }

        // Verifica que las fechas existan y sean correctas.
        if (fechaInicio == null || fechaVencimiento == null
                || fechaVencimiento.before(fechaInicio)) {

            throw new IllegalArgumentException(
                    "Las fechas del préstamo no son válidas."
            );
        }

        // this hace referencia al objeto actual.
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;

        // Calcula el monto total y el valor de cada cuota.
        calcularValores();

        // Inicialmente el saldo pendiente es el total del préstamo.
        this.saldoPendiente = montoTotal;

        // Todo préstamo nuevo comienza en estado pendiente.
        this.estado = EstadoPrestamo.PENDIENTE;
    }


    /*
     * MÉTODO calcularValores()
     *
     * Calcula el valor del interés, el monto total que debe pagar
     * el cliente y el valor de cada cuota.
     */
    public void calcularValores() {

        // Calcula el valor correspondiente al interés.
        double valorInteres = monto * interes / 100;

        // Suma el monto inicial más los intereses.
        montoTotal = monto + valorInteres;

        // Redondea el monto total a dos decimales.
        montoTotal = Math.round(montoTotal * 100.0) / 100.0;

        // Divide el monto total entre el número de cuotas.
        valorCuota = montoTotal / cuotas;

        // Redondea el valor de cada cuota a dos decimales.
        valorCuota = Math.round(valorCuota * 100.0) / 100.0;
    }


    /*
     * MÉTODOS GETTERS Y SETTERS
     *
     * Los getters permiten consultar los atributos privados.
     * Los setters permiten modificar sus valores.
     *
     * Se utilizan para aplicar el encapsulamiento.
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getMonto() {
        return monto;
    }

    public double getInteres() {
        return interes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }


    /*
     * SETTER DEL SALDO PENDIENTE
     *
     * Permite actualizar el saldo después de registrar un pago.
     * No permite que el saldo sea negativo ni mayor al monto total.
     *
     * Si el saldo llega a cero, el préstamo queda PAGADO.
     * De lo contrario, permanece PENDIENTE.
     */
    public void setSaldoPendiente(double saldoPendiente) {

        if (saldoPendiente < 0 || saldoPendiente > montoTotal) {
            throw new IllegalArgumentException(
                    "El saldo pendiente no es válido."
            );
        }

        this.saldoPendiente =
                Math.round(saldoPendiente * 100.0) / 100.0;

        if (this.saldoPendiente == 0) {
            this.estado = EstadoPrestamo.PAGADO;
        } else {
            this.estado = EstadoPrestamo.PENDIENTE;
        }
    }


    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }


    /*
     * MÉTODO toString()
     *
     * Permite obtener una representación en texto del préstamo.
     * Es útil para mostrar la información en pantalla.
     */
    @Override
    public String toString() {

        return "PRESTAMO"
                + "\nID: " + id
                + "\nCliente: " + cliente.getNombre()
                + "\nEmpleado: " + empleado.getNombre()
                + "\nMonto: " + monto
                + "\nInteres: " + interes + "%"
                + "\nCuotas: " + cuotas
                + "\nMonto total: " + montoTotal
                + "\nValor de cuota: " + valorCuota
                + "\nSaldo pendiente: " + saldoPendiente
                + "\nFecha de inicio: " + fechaInicio
                + "\nFecha de vencimiento: " + fechaVencimiento
                + "\nEstado: " + estado;
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve Cliente cliente?
     * Respuesta: Para asociar el préstamo con el cliente
     * que recibe el crédito.
     *
     * 2. ¿Para qué sirve Empleado empleado?
     * Respuesta: Para identificar al trabajador que
     * gestiona el préstamo.
     *
     * 3. ¿Por qué utilizas private?
     * Respuesta: Para aplicar encapsulamiento y controlar
     * el acceso a los atributos.
     *
     * 4. ¿Para qué sirve calcularValores()?
     * Respuesta: Para calcular intereses, monto total
     * y cuotas.
     *
     * 5. ¿Para qué sirve this?
     * Respuesta: Para hacer referencia al objeto actual.
     *
     * 6. ¿Para qué sirve EstadoPrestamo?
     * Respuesta: Para representar si el préstamo está
     * pendiente o pagado.
     *
     * 7. ¿Para qué sirve toString()?
     * Respuesta: Para mostrar la información del préstamo.
     *
     * =====================================================
     */
}