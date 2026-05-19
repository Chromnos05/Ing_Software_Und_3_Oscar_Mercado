package com.nomina.excepcion;

/**
 * Excepcion lanzada cuando se intenta registrar o calcular con un monto
 * de ventas invalido.
 *
 * <p>Un monto de ventas es invalido si es negativo o si supera algun limite
 * de negocio definido (e.g., tope maximo de ventas reportables).</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 */
public class VentasInvalidasException extends Exception {

    /** Identificador de version de serializacion. */
    private static final long serialVersionUID = 1L;

    /** Monto de ventas que provoco la excepcion. */
    private final double montoVentas;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripcion del error
     */
    public VentasInvalidasException(String mensaje) {
        super(mensaje);
        this.montoVentas = 0.0;
    }

    /**
     * Constructor con mensaje y monto de ventas que origino el error.
     *
     * @param mensaje     descripcion del error
     * @param montoVentas monto de ventas que se intento registrar
     */
    public VentasInvalidasException(String mensaje, double montoVentas) {
        super(String.format("%s [Monto de ventas: %.2f]", mensaje, montoVentas));
        this.montoVentas = montoVentas;
    }

    /**
     * Constructor con causa raiz.
     *
     * @param mensaje descripcion del error
     * @param causa   excepcion que origino este error
     */
    public VentasInvalidasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.montoVentas = 0.0;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna el monto de ventas que desencadeno la excepcion.
     *
     * @return monto de ventas ingresado
     */
    public double getMontoVentas() {
        return montoVentas;
    }
}

