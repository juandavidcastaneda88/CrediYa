package com.crediya.pagos.excepciones;

/**
 * Se lanza cuando se intenta operar sobre un préstamo que no existe.
 */
public class PrestamoNoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public PrestamoNoEncontradoException(int idPrestamo) {
        super("No se encontró el préstamo con ID " + idPrestamo + ".");
    }
}
