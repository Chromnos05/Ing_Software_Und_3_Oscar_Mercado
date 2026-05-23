package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Empleado de sueldo fijo. Sin importar horas o ventas,
 * siempre recibe el mismo salario base cada mes.
 * Tiene prima de servicios y descuentos de ley (salud y pension).
 */
public class EmpleadoAsalariado extends Empleado {

    private static final double PORCENTAJE_SALUD       = 0.04;
    private static final double PORCENTAJE_PENSION      = 0.04;
    private static final double PORCENTAJE_PRIMA        = 0.0833; // 1/12 del sueldo

    private double salarioBase;

    public EmpleadoAsalariado(String id,
                              String nombre,
                              LocalDate fechaIngreso,
                              double salarioBase) {
        super(id, nombre, fechaIngreso, TipoEmpleado.ASALARIADO);
        setSalarioBase(salarioBase);
    }

    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        return salarioBase;
    }

    /** Prima de servicios: ~8.33% del salario (una doceava parte). */
    @Override
    public double calcularBeneficios() {
        return salarioBase * PORCENTAJE_PRIMA;
    }

    /** Descuento de salud (4%) + pension (4%). */
    @Override
    public double calcularDeducciones() {
        return salarioBase * (PORCENTAJE_SALUD + PORCENTAJE_PENSION);
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public final void setSalarioBase(double salarioBase) {
        if (salarioBase < 0) {
            throw new IllegalArgumentException(
                    "El salario base no puede ser negativo: " + salarioBase);
        }
        this.salarioBase = salarioBase;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", salarioBase=%.2f", salarioBase);
    }
}
