package com.nomina.excepcion;

/**
 * Excepción lanzada cuando se intenta registrar o calcular con una cantidad
 * de horas trabajadas inválida.
 *
 * <p>Son inválidas las horas que sean negativas o que superen el máximo legal
 * permitido (e.g., más de 24 horas en un día o más de 168 en una semana).</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public class HorasInvalidasException extends Exception {

    /** Identificador de versión de serialización. */
    private static final long serialVersionUID = 1L;

    /** Cantidad de horas que provocó la excepción. */
    private final double horasIngresadas;

    /** Límite máximo de horas permitido. */
    private final double horasMaximas;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public HorasInvalidasException(String mensaje) {
        super(mensaje);
        this.horasIngresadas = 0.0;
        this.horasMaximas    = 0.0;
    }

    /**
     * Constructor con mensaje y valor de horas que originó el error.
     *
     * @param mensaje         descripción del error
     * @param horasIngresadas número de horas que se intentó registrar
     * @param horasMaximas    límite máximo permitido
     */
    public HorasInvalidasException(String mensaje,
                                   double horasIngresadas,
                                   double horasMaximas) {
        super(String.format("%s [Horas ingresadas: %.2f | Máximo permitido: %.2f]",
                mensaje, horasIngresadas, horasMaximas));
        this.horasIngresadas = horasIngresadas;
        this.horasMaximas    = horasMaximas;
    }

    /**
     * Constructor con causa raíz.
     *
     * @param mensaje descripción del error
     * @param causa   excepción que originó este error
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
     * Retorna el límite máximo de horas permitido.
     *
     * @return horas máximas
     */
    public double getHorasMaximas() {
        return horasMaximas;
    }
}
