package com.nomina.excepcion;

/**
 * Excepción lanzada cuando se intenta registrar o calcular con un monto
 * de ventas inválido.
 *
 * <p>Un monto de ventas es inválido si es negativo o si supera algún límite
 * de negocio definido (e.g., tope máximo de ventas reportables).</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public class VentasInvalidasException extends Exception {

    /** Identificador de versión de serialización. */
    private static final long serialVersionUID = 1L;

    /** Monto de ventas que provocó la excepción. */
    private final double montoVentas;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public VentasInvalidasException(String mensaje) {
        super(mensaje);
        this.montoVentas = 0.0;
    }

    /**
     * Constructor con mensaje y monto de ventas que originó el error.
     *
     * @param mensaje     descripción del error
     * @param montoVentas monto de ventas que se intentó registrar
     */
    public VentasInvalidasException(String mensaje, double montoVentas) {
        super(String.format("%s [Monto de ventas: %.2f]", mensaje, montoVentas));
        this.montoVentas = montoVentas;
    }

    /**
     * Constructor con causa raíz.
     *
     * @param mensaje descripción del error
     * @param causa   excepción que originó este error
     */
    public VentasInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.montoVentas = 0.0;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna el monto de ventas que desencadenó la excepción.
     *
     * @return monto de ventas ingresado
     */
    public double getMontoVentas() {
        return montoVentas;
    }
}
