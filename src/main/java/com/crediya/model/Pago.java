package com.crediya.model;

import java.sql.Date;

/*
 * CLASE: Pago
 *
 * Representa los pagos realizados por los clientes
 * para abonar a sus préstamos en CrediYa S.A.S.
 *
 * Cada pago está relacionado con un préstamo.
 *
 * Esta clase permite almacenar el préstamo al que pertenece
 * el pago, la fecha en que se realizó y el monto pagado.
 */
public class Pago {

    /*
     * ATRIBUTOS
     *
     * Se utiliza private para aplicar ENCAPSULAMIENTO.
     * Esto permite proteger los atributos y controlar
     * su acceso mediante los métodos correspondientes.
     */

    private int id;

    // Préstamo al que pertenece este pago.
    private Prestamo prestamo;

    // Fecha en la que se realizó el pago.
    private Date fechaPago;

    // Cantidad de dinero que pagó el cliente.
    private double monto;


    /*
     * PRIMER CONSTRUCTOR
     *
     * Se utiliza cuando se registra un pago nuevo.
     *
     * El valor 0 indica que el pago todavía no tiene
     * un identificador asignado por la base de datos.
     *
     * this(...) llama al segundo constructor de esta clase
     * para reutilizar la inicialización de los atributos.
     */
    public Pago(
            Prestamo prestamo,
            Date fechaPago,
            double monto) {

        this(0, prestamo, fechaPago, monto);
    }


    /*
     * SEGUNDO CONSTRUCTOR
     *
     * Se utiliza cuando se crea un pago indicando todos
     * sus datos, incluido el identificador.
     *
     * También permite representar un pago recuperado
     * desde la base de datos.
     */
    public Pago(
            int id,
            Prestamo prestamo,
            Date fechaPago,
            double monto) {

        // Verifica que el pago esté relacionado con un préstamo.
        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "El pago debe estar asociado a un préstamo."
            );
        }

        // Asigna los valores iniciales del objeto.
        this.id = id;
        this.prestamo = prestamo;

        // Utilizamos los setters para validar los datos.
        setFechaPago(fechaPago);
        setMonto(monto);
    }


    /*
     * GETTER Y SETTER DEL IDENTIFICADOR
     *
     * El getter permite consultar el ID del pago.
     * El setter permite modificarlo.
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /*
     * GETTER DEL PRÉSTAMO
     *
     * Permite consultar el préstamo al que pertenece
     * el pago.
     *
     * Se utiliza el objeto Prestamo para mantener
     * la relación entre ambas clases.
     */
    public Prestamo getPrestamo() {
        return prestamo;
    }


    /*
     * GETTER Y SETTER DE LA FECHA DE PAGO
     *
     * El getter permite consultar la fecha.
     * El setter permite modificarla.
     *
     * Se utiliza java.sql.Date porque la base de datos
     * almacena esta información mediante el tipo DATE.
     */

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {

        // La fecha del pago es obligatoria.
        if (fechaPago == null) {
            throw new IllegalArgumentException(
                    "La fecha del pago es obligatoria."
            );
        }

        this.fechaPago = fechaPago;
    }


    /*
     * GETTER Y SETTER DEL MONTO
     *
     * El getter permite consultar cuánto dinero se pagó.
     * El setter permite modificar el monto.
     *
     * Se valida que el pago sea mayor que cero.
     */
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {

        if (monto <= 0) {
            throw new IllegalArgumentException(
                    "El monto del pago debe ser mayor que cero."
            );
        }

        this.monto = monto;
    }


    /*
     * MÉTODO toString()
     *
     * Permite obtener una representación en texto del pago.
     *
     * Mostramos el identificador del préstamo para conocer
     * a cuál crédito corresponde este pago.
     */
    @Override
    public String toString() {

        return "Pago{"
                + "id=" + id
                + ", prestamoId=" + prestamo.getId()
                + ", fechaPago=" + fechaPago
                + ", monto=" + monto
                + '}';
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve la clase Pago?
     * Respuesta: Para representar los pagos que realizan
     * los clientes para abonar a sus préstamos.
     *
     * 2. ¿Para qué sirve Prestamo prestamo?
     * Respuesta: Para relacionar cada pago con el préstamo
     * al que pertenece.
     *
     * 3. ¿Por qué utilizamos private?
     * Respuesta: Para aplicar encapsulamiento y proteger
     * los atributos de la clase.
     *
     * 4. ¿Para qué sirve this()?
     * Respuesta: Para llamar a otro constructor de la misma
     * clase y reutilizar su código.
     *
     * 5. ¿Para qué sirve this?
     * Respuesta: Para hacer referencia al objeto actual.
     *
     * 6. ¿Por qué utilizamos Date?
     * Respuesta: Para almacenar la fecha en la que
     * se realizó el pago.
     *
     * 7. ¿Por qué utilizamos java.sql.Date?
     * Respuesta: Porque permite trabajar con fechas
     * compatibles con el tipo DATE de MySQL.
     *
     * 8. ¿Para qué sirve setMonto()?
     * Respuesta: Para asignar el monto del pago y verificar
     * que sea mayor que cero.
     *
     * 9. ¿Por qué verificamos que Prestamo no sea null?
     * Respuesta: Porque todo pago debe estar asociado
     * a un préstamo existente.
     *
     * 10. ¿Para qué sirve getPrestamo()?
     * Respuesta: Para consultar el préstamo al que
     * pertenece el pago.
     *
     * 11. ¿Para qué sirve toString()?
     * Respuesta: Para mostrar la información del pago
     * en forma de texto.
     *
     * 12. ¿Qué relación existe entre Pago y Prestamo?
     * Respuesta: Un pago pertenece a un préstamo.
     * Un préstamo puede tener varios pagos.
     *
     * =====================================================
     */
}