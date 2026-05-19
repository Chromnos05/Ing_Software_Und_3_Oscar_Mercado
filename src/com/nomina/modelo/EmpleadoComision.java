package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.excepcion.VentasInvalidasException;
import java.time.LocalDate;

/**
 * Representa a un empleado cuya remuneracion principal proviene de una
 * comision calculada sobre el monto de ventas realizadas en el periodo.
 *
 * <p>El salario bruto se compone de un salario base fijo mas un porcentaje
 * de comision sobre las ventas totales. A mayor volumen de ventas, mayor
 * remuneracion variable.</p>
 *
 * <p>Principio SRP: esta clase es responsable unicamente de modelar
 * la logica salarial del empleado por comision.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 * @see Empleado
 * @see VentasInvalidasException
 */
public class EmpleadoComision extends Empleado {

    // ── Constantes ─────────────────────────────────────────────────────────

    /** Porcentaje de aporte a salud (4 %). */
    private static final double PORCENTAJE_SALUD    = 0.04;

    /** Porcentaje de aporte a pension (4 %). */
    private static final double PORCENTAJE_PENSION  = 0.04;

    /** Bono por cumplimiento de meta de ventas. */
    private static final double BONO_META           = 200_000.0;

    /** Monto de ventas minimo para recibir el bono de meta. */
    private static final double META_VENTAS         = 10_000_000.0;

    /** Monto maximo de ventas aceptable en un periodo (limite de negocio). */
    public static final double VENTAS_MAXIMAS       = 500_000_000.0;

    // ── Atributos privados ─────────────────────────────────────────────────

    /** Salario base fijo mensual (≥ 0). */
    private double salarioBase;

    /** Porcentaje de comision sobre ventas, expresado como decimal (0–1). */
    private double porcentajeComision;

    /** Monto total de ventas realizadas en el periodo (0 ≤ valor ≤ VENTAS_MAXIMAS). */
    private double totalVentas;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un empleado por comision.
     *
     * @param id                  identificador unico
     * @param nombre              nombre completo
     * @param fechaIngreso        fecha de ingreso a la empresa
     * @param salarioBase         salario base fijo (≥ 0)
     * @param porcentajeComision  porcentaje de comision, expresado como decimal (e.g. 0.05 = 5 %)
     * @param totalVentas         monto total de ventas del periodo (0 a {@value #VENTAS_MAXIMAS})
     * @throws IllegalArgumentException si salarioBase o porcentajeComision son invalidos
     * @throws VentasInvalidasException si totalVentas esta fuera del rango permitido
     */
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

    // ── Implementacion de metodos abstractos ───────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Formula: {@code salarioBruto = salarioBase + (totalVentas × porcentajeComision)}</p>
     *
     * @return salario bruto calculado con base en ventas y comision
     * @throws SalarioNegativoException si el resultado es negativo (no deberia ocurrir)
     */
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

    /**
     * {@inheritDoc}
     *
     * <p>Beneficio: bono de {@value #BONO_META} si las ventas superan la
     * meta de {@value #META_VENTAS}.</p>
     *
     * @return monto del bono por cumplimiento de meta (0 si no aplica)
     */
    @Override
    public double calcularBeneficios() {
        return (totalVentas >= META_VENTAS) ? BONO_META : 0.0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Deducciones: aporte a salud + pension sobre el salario bruto.</p>
     *
     * @return total de deducciones de seguridad social
     */
    @Override
    public double calcularDeducciones() {
        try {
            return calcularSalarioBruto() * (PORCENTAJE_SALUD + PORCENTAJE_PENSION);
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
    }

    // ── Getters y Setters con validacion ───────────────────────────────────

    /**
     * Retorna el salario base fijo mensual.
     *
     * @return salario base
     */
    public double getSalarioBase() {
        return salarioBase;
    }

    /**
     * Establece el salario base fijo mensual.
     *
     * @param salarioBase nuevo salario base (≥ 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public final void setSalarioBase(double salarioBase) {
        if (salarioBase < 0) {
            throw new IllegalArgumentException(
                    "El salario base no puede ser negativo: " + salarioBase);
        }
        this.salarioBase = salarioBase;
    }

    /**
     * Retorna el porcentaje de comision como decimal.
     *
     * @return porcentaje de comision (e.g. 0.05 representa el 5 %)
     */
    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    /**
     * Establece el porcentaje de comision.
     *
     * @param porcentajeComision porcentaje en decimal (0 a 1)
     * @throws IllegalArgumentException si esta fuera del rango [0, 1]
     */
    public final void setPorcentajeComision(double porcentajeComision) {
        if (porcentajeComision < 0 || porcentajeComision > 1) {
            throw new IllegalArgumentException(
                    "El porcentaje de comision debe estar entre 0 y 1: " + porcentajeComision);
        }
        this.porcentajeComision = porcentajeComision;
    }

    /**
     * Retorna el total de ventas del periodo.
     *
     * @return total de ventas
     */
    public double getTotalVentas() {
        return totalVentas;
    }

    /**
     * Establece el total de ventas del periodo.
     *
     * @param totalVentas monto de ventas (0 ≤ valor ≤ {@value #VENTAS_MAXIMAS})
     * @throws VentasInvalidasException si el monto esta fuera del rango valido
     */
    public final void setTotalVentas(double totalVentas) throws VentasInvalidasException {
        if (totalVentas < 0 || totalVentas > VENTAS_MAXIMAS) {
            throw new VentasInvalidasException(
                    "Monto de ventas invalido para empleado: " + getNombre(), totalVentas);
        }
        this.totalVentas = totalVentas;
    }

    /**
     * Representacion textual del empleado por comision.
     *
     * @return cadena con datos basicos, salario base, comision y ventas
     */
    @Override
    public String toString() {
        return super.toString() + String.format(
                ", salarioBase=%.2f, comision=%.0f%%, totalVentas=%.2f",
                salarioBase, porcentajeComision * 100, totalVentas);
    }
}

