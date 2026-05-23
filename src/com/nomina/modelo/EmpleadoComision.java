package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.excepcion.VentasInvalidasException;
import java.time.LocalDate;

/**
 * Empleado que vive de las ventas. Recibe un salario base
 * mas un porcentaje de comision sobre lo que vende.
 * Si alcanza la meta de ventas, se gana un bono extra.
 */
public class EmpleadoComision extends Empleado {

    private static final double PORCENTAJE_SALUD    = 0.04;
    private static final double PORCENTAJE_PENSION  = 0.04;
    private static final double BONO_META           = 200_000.0;
    private static final double META_VENTAS         = 10_000_000.0;

    public static final double VENTAS_MAXIMAS       = 500_000_000.0;

    private double salarioBase;
    private double porcentajeComision;
    private double totalVentas;

    public EmpleadoComision(String id,
                            String nombre,
                            LocalDate fechaIngreso,
                            double salarioBase,
                            double porcentajeComision,
                            double totalVentas) throws VentasInvalidasException {
        super(id, nombre, fechaIngreso, TipoEmpleado.COMISION);
        setSalarioBase(salarioBase);
        setPorcentajeComision(porcentajeComision);
        setTotalVentas(totalVentas);
    }

    /** Salario base + la comision generada por las ventas del periodo. */
    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        double comision = totalVentas * porcentajeComision;
        double bruto    = salarioBase + comision;
        if (bruto < 0) {
            throw new SalarioNegativoException(
                    "Salario bruto negativo para empleado: " + getNombre(), bruto);
        }
        return bruto;
    }

    /** Bono de $200,000 si las ventas alcanzan o superan los $10,000,000. */
    @Override
    public double calcularBeneficios() {
        return (totalVentas >= META_VENTAS) ? BONO_META : 0.0;
    }

    /** Descuento de salud + pension sobre el salario bruto. */
    @Override
    public double calcularDeducciones() {
        try {
            return calcularSalarioBruto() * (PORCENTAJE_SALUD + PORCENTAJE_PENSION);
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
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

    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    public final void setPorcentajeComision(double porcentajeComision) {
        if (porcentajeComision < 0 || porcentajeComision > 1) {
            throw new IllegalArgumentException(
                    "El porcentaje de comision debe estar entre 0 y 1: " + porcentajeComision);
        }
        this.porcentajeComision = porcentajeComision;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public final void setTotalVentas(double totalVentas) throws VentasInvalidasException {
        if (totalVentas < 0 || totalVentas > VENTAS_MAXIMAS) {
            throw new VentasInvalidasException(
                    "Monto de ventas invalido para empleado: " + getNombre(), totalVentas);
        }
        this.totalVentas = totalVentas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", salarioBase=%.2f, comision=%.0f%%, totalVentas=%.2f",
                salarioBase, porcentajeComision * 100, totalVentas);
    }
}
