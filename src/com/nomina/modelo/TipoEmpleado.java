package com.nomina.modelo;

/**
 * Las 4 formas de contratacion que maneja el sistema.
 * Cada tipo define como se calcula el sueldo del empleado.
 */
public enum TipoEmpleado {

    ASALARIADO("Asalariado"),
    POR_HORAS("Por Horas"),
    COMISION("Por Comision"),
    TEMPORAL("Temporal");

    private final String descripcion;

    TipoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
