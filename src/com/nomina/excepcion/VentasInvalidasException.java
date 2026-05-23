package com.nomina.excepcion;

/**
 * Se reporto un monto de ventas que no tiene sentido:
 * negativo o por encima del tope definido por la empresa.
 */
public class VentasInvalidasException extends Exception {

    private static final long serialVersionUID = 1L;

    private final double montoVentas;

    public VentasInvalidasException(String mensaje) {
        super(mensaje);
        this.montoVentas = 0.0;
    }

    public VentasInvalidasException(String mensaje, double montoVentas) {
        super(String.format("%s [Monto de ventas: %.2f]", mensaje, montoVentas));
        this.montoVentas = montoVentas;
    }

    public VentasInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.montoVentas = 0.0;
    }

    public double getMontoVentas() {
        return montoVentas;
    }
}
