package com.nomina.modelo;

/**
 * Enumeración que define los tipos de empleado disponibles en el sistema de nómina.
 *
 * <p>Cada constante representa una modalidad contractual distinta que determina
 * cómo se calculan el salario bruto, los beneficios y las deducciones.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public enum TipoEmpleado {

    /**
     * Empleado con salario fijo mensual, independiente de las horas trabajadas.
     */
    ASALARIADO("Asalariado"),

    /**
     * Empleado cuya remuneración se calcula según las horas trabajadas y
     * una tarifa por hora acordada.
     */
    POR_HORAS("Por Horas"),

    /**
     * Empleado cuya remuneración principal proviene de un porcentaje
     * sobre el monto de ventas realizadas.
     */
    COMISION("Por Comisión"),

    /**
     * Empleado contratado de forma temporal por un período definido,
     * sin beneficios de largo plazo.
     */
    TEMPORAL("Temporal");

    // ── Atributos ──────────────────────────────────────────────────────────

    /** Descripción legible del tipo de empleado. */
    private final String descripcion;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Constructor del enum.
     *
     * @param descripcion descripción legible del tipo de empleado
     */
    TipoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna la descripción legible del tipo de empleado.
     *
     * @return descripción del tipo de empleado
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Representación textual del tipo de empleado.
     *
     * @return descripción del tipo de empleado
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
