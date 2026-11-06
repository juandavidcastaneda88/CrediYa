package com.crediya.model;

/*
 * ENUMERACIÓN: EstadoPrestamo
 *
 * Representa los posibles estados de un préstamo
 * dentro del sistema CrediYa S.A.S.
 *
 * Se utiliza enum para definir un conjunto limitado
 * de valores constantes.
 *
 * En este proyecto manejamos dos estados:
 *
 * PENDIENTE: el préstamo todavía tiene un saldo por pagar.
 *
 * PAGADO: el préstamo ya fue cancelado completamente.
 *
 * Esta enumeración permite trabajar con estados definidos
 * y evita utilizar textos diferentes para representar
 * la misma situación.
 */
public enum EstadoPrestamo {

    /*
     * CONSTANTE PENDIENTE
     *
     * Representa un préstamo que todavía tiene
     * un saldo pendiente de pago.
     */
    PENDIENTE,

    /*
     * CONSTANTE PAGADO
     *
     * Representa un préstamo que ya fue cancelado
     * completamente por el cliente.
     */
    PAGADO;


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Qué es un enum?
     * Respuesta: Es un tipo de dato especial de Java que
     * permite definir un conjunto de constantes.
     *
     * 2. ¿Para qué sirve EstadoPrestamo?
     * Respuesta: Para representar los posibles estados
     * de un préstamo dentro del sistema CrediYa.
     *
     * 3. ¿Qué significa PENDIENTE?
     * Respuesta: Significa que el préstamo todavía
     * tiene un saldo por pagar.
     *
     * 4. ¿Qué significa PAGADO?
     * Respuesta: Significa que el préstamo fue
     * cancelado completamente.
     *
     * 5. ¿Por qué utilizamos enum en lugar de String?
     * Respuesta: Porque enum permite trabajar con valores
     * definidos y evita escribir estados diferentes
     * por errores de escritura.
     *
     * 6. ¿Dónde se utiliza EstadoPrestamo?
     * Respuesta: Se utiliza en la clase Prestamo
     * para identificar el estado actual del crédito.
     *
     * 7. ¿Qué relación tiene con la clase Prestamo?
     * Respuesta: Prestamo tiene un atributo de tipo
     * EstadoPrestamo que permite conocer si está pendiente
     * o pagado.
     *
     * 8. ¿Por qué las palabras PENDIENTE y PAGADO
     * están escritas en mayúsculas?
     * Respuesta: Porque las constantes en Java
     * normalmente se escriben en mayúsculas para
     * diferenciarlas de las variables.
     *
     * =====================================================
     */
}