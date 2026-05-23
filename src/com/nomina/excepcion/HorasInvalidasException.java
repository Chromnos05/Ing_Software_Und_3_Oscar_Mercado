package com.nomina.excepcion;

/**
 * Se intentaron registrar horas imposibles:
 * negativas o que se pasan del maximo permitido.
 */
public class HorasInvalidasException extends Exception {

    private static final long serialVersionUID = 1L;

    private final double horasIngresadas;
    private final double horasMaximas;

    public HorasInvalidasException(String mensaje) {
        super(mensaje);
        this.horasIngresadas = 0.0;
        this.horasMaximas    = 0.0;
    }

    public HorasInvalidasException(String mensaje,
                                   double horasIngresadas,
                                   double horasMaximas) {
        super(String.format("%s [Horas ingresadas: %.2f | Maximo permitido: %.2f]",
                mensaje, horasIngresadas, horasMaximas));
        this.horasIngresadas = horasIngresadas;
        this.horasMaximas    = horasMaximas;
    }

    public HorasInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.horasIngresadas = 0.0;
        this.horasMaximas    = 0.0;
    }

    public double getHorasIngresadas() {
        return horasIngresadas;
    }

    public double getHorasMaximas() {
        return horasMaximas;
    }
}
