package com.nomina.excepcion;

/**
 * Excepcion lanzada cuando se intenta registrar o calcular con una cantidad
 * de horas trabajadas invalida.
 *
 * <p>Son invalidas las horas que sean negativas o que superen el maximo legal
 * permitido (e.g., mas de 24 horas en un dia o mas de 168 en una semana).</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 */
public class HorasInvalidasException extends Exception {

    /** Identificador de version de serializacion. */
    private static final long serialVersionUID = 1L;

    /** Cantidad de horas que provoco la excepcion. */
    private final double horasIngresadas;

    /** Limite maximo de horas permitido. */
    private final double horasMaximas;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripcion del error
     */
    public HorasInvalidasException(String mensaje) {
        super(mensaje);
        this.horasIngresadas = 0.0;
        this.horasMaximas    = 0.0;
    }

    /**
     * Constructor con mensaje y valor de horas que origino el error.
     *
     * @param mensaje         descripcion del error
     * @param horasIngresadas numero de horas que se intento registrar
     * @param horasMaximas    limite maximo permitido
     */
    public HorasInvalidasException(String mensaje,
                                   double horasIngresadas,
                                   double horasMaximas) {
        super(String.format("%s [Horas ingresadas: %.2f | Maximo permitido: %.2f]",
                mensaje, horasIngresadas, horasMaximas));
        this.horasIngresadas = horasIngresadas;
        this.horasMaximas    = horasMaximas;
    }

    /**
     * Constructor con causa raiz.
     *
     * @param mensaje descripcion del error
     * @param causa   excepcion que origino este error
     */
    public HorasInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.horasIngresadas = 0.0;
        this.horasMaximas    = 0.0;
    }

    // ── Getters ────────────────────────────────────────────────────────────

    /**
     * Retorna las horas que se intentaron registrar.
     *
     * @return horas ingresadas
     */
    public double getHorasIngresadas() {
        return horasIngresadas;
    }

    /**
     * Retorna el limite maximo de horas permitido.
     *
     * @return horas maximas
     */
    public double getHorasMaximas() {
        return horasMaximas;
    }
}

