package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Representa a un empleado asalariado que recibe un salario fijo mensual.
 *
 * <p>El salario bruto es constante y acordado contractualmente.
 * Los beneficios incluyen prima de servicios y los aportes a seguridad
 * social conforman las deducciones.</p>
 *
 * <p>Principio SRP: esta clase es responsable unicamente de modelar
 * la logica salarial del empleado de tipo asalariado.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 * @see Empleado
 */
public class EmpleadoAsalariado extends Empleado {

    // ── Constantes ─────────────────────────────────────────────────────────

    /** Porcentaje de aporte a salud sobre el salario bruto (4 %). */
    private static final double PORCENTAJE_SALUD       = 0.04;

    /** Porcentaje de aporte a pension sobre el salario bruto (4 %). */
    private static final double PORCENTAJE_PENSION      = 0.04;

    /** Porcentaje de prima de servicios (8.33 % mensual ≈ 1/12 de un sueldo). */
    private static final double PORCENTAJE_PRIMA        = 0.0833;

    // ── Atributos privados ─────────────────────────────────────────────────

    /** Salario base mensual pactado en el contrato (valor ≥ 0). */
    private double salarioBase;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un empleado asalariado.
     *
     * @param id           identificador unico
     * @param nombre       nombre completo
     * @param fechaIngreso fecha de ingreso a la empresa
     * @param salarioBase  salario base mensual (debe ser ≥ 0)
     * @throws IllegalArgumentException si el salario base es negativo
     */
    public EmpleadoAsalariado(String id,
                              String nombre,
                              LocalDate fechaIngreso,
                              double salarioBase) {
        super(id, nombre, fechaIngreso, TipoEmpleado.ASALARIADO);
        setSalarioBase(salarioBase);
    }

    // ── Implementacion de metodos abstractos ───────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Para el empleado asalariado, el salario bruto es igual al salario base.</p>
     *
     * @return salario base mensual
     * @throws SalarioNegativoException nunca (el setter ya garantiza valor ≥ 0)
     */
    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        return salarioBase;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Beneficio: prima de servicios = {@value #PORCENTAJE_PRIMA} × salario base.</p>
     *
     * @return monto de prima de servicios
     */
    @Override
    public double calcularBeneficios() {
        return salarioBase * PORCENTAJE_PRIMA;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Deducciones: aporte a salud ({@value #PORCENTAJE_SALUD})
     * + aporte a pension ({@value #PORCENTAJE_PENSION}) sobre el salario base.</p>
     *
     * @return total de deducciones
     */
    @Override
    public double calcularDeducciones() {
        return salarioBase * (PORCENTAJE_SALUD + PORCENTAJE_PENSION);
    }

    // ── Getter y Setter con validacion ─────────────────────────────────────

    /**
     * Retorna el salario base mensual.
     *
     * @return salario base
     */
    public double getSalarioBase() {
        return salarioBase;
    }

    /**
     * Establece el salario base mensual.
     *
     * @param salarioBase nuevo salario base (debe ser ≥ 0)
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
     * Representacion textual del empleado asalariado.
     *
     * @return cadena con datos del empleado y su salario base
     */
    @Override
    public String toString() {
        return super.toString() + String.format(", salarioBase=%.2f", salarioBase);
    }
}

