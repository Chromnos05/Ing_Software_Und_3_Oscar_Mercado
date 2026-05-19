package com.nomina.modelo;

/**
 * Enumeracion que define los tipos de empleado disponibles en el sistema de nomina.
 *
 * <p>Cada constante representa una modalidad contractual distinta que determina
 * como se calculan el salario bruto, los beneficios y las deducciones.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 */
public enum TipoEmpleado {

    /**
     * Empleado con salario fijo mensual, independiente de las horas trabajadas.
     */
    ASALARIADO("Asalariado"),

    /**
     * Empleado cuya remuneracion se calcula segun las horas trabajadas y
     * una tarifa por hora acordada.
     */
    POR_HORAS("Por Horas"),

    /**
     * Empleado cuya remuneracion principal proviene de un porcentaje
     * sobre el monto de ventas realizadas.
     */
    COMISION("Por Comision"),

    /**
     * Empleado contratado de forma temporal por un periodo definido,
     * sin beneficios de largo plazo.
     */
    TEMPORAL("Temporal");

    // ── Atributos ──────────────────────────────────────────────────────────

    /** Descripcion legible del tipo de empleado. */
    private final String descripcion;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Constructor del enum.
     *
     * @param descripcion descripcion legible del tipo de empleado
     */
    TipoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna la descripcion legible del tipo de empleado.
     *
     * @return descripcion del tipo de empleado
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Representacion textual del tipo de empleado.
     *
     * @return descripcion del tipo de empleado
     */
    @Override
    public String toString() {
        return descripcion;
    }
}

