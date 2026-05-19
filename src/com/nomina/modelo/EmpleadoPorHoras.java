package com.nomina.modelo;

import com.nomina.excepcion.HorasInvalidasException;
import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Representa a un empleado remunerado por horas trabajadas.
 *
 * <p>El salario bruto se calcula multiplicando las horas trabajadas en el período
 * por la tarifa pactada por hora. Las horas extras (superiores a la jornada
 * estándar) se pagan con un recargo adicional.</p>
 *
 * <p>Principio SRP: esta clase es responsable únicamente de modelar
 * la lógica salarial del empleado contratado por horas.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 * @see Empleado
 * @see HorasInvalidasException
 */
public class EmpleadoPorHoras extends Empleado {

    // ── Constantes ─────────────────────────────────────────────────────────

    /** Máximo de horas trabajables en un período mensual (4 semanas × 48 h). */
    public static final double HORAS_MAXIMAS_MENSUAL    = 192.0;

    /** Horas que conforman la jornada ordinaria mensual. */
    public static final double HORAS_JORNADA_ORDINARIA  = 160.0;

    /** Factor de recargo sobre la tarifa por hora para horas extras (50 %). */
    public static final double FACTOR_HORA_EXTRA        = 1.50;

    /** Porcentaje de aporte a salud (4 %). */
    private static final double PORCENTAJE_SALUD        = 0.04;

    /** Porcentaje de aporte a pensión (4 %). */
    private static final double PORCENTAJE_PENSION      = 0.04;

    /** Bonificación fija por auxiliar de transporte si aplica. */
    private static final double AUXILIO_TRANSPORTE      = 117172.0;

    /** Umbral salarial para recibir auxilio de transporte (2 × SMMLV aprox.). */
    private static final double UMBRAL_AUXILIO          = 2_600_000.0;

    // ── Atributos privados ─────────────────────────────────────────────────

    /** Tarifa de pago por cada hora ordinaria trabajada (valor ≥ 0). */
    private double tarifaPorHora;

    /** Horas trabajadas en el período actual (0 ≤ valor ≤ HORAS_MAXIMAS_MENSUAL). */
    private double horasTrabajadas;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un empleado por horas.
     *
     * @param id              identificador único
     * @param nombre          nombre completo
     * @param fechaIngreso    fecha de ingreso a la empresa
     * @param tarifaPorHora   tarifa por hora ordinaria (≥ 0)
     * @param horasTrabajadas horas trabajadas en el período (0 a 192)
     * @throws IllegalArgumentException si tarifaPorHora es negativa
     * @throws HorasInvalidasException  si horasTrabajadas está fuera del rango válido
     */
    public EmpleadoPorHoras(String id,
                            String nombre,
                            LocalDate fechaIngreso,
                            double tarifaPorHora,
                            double horasTrabajadas) throws HorasInvalidasException {
        super(id, nombre, fechaIngreso, TipoEmpleado.POR_HORAS);
        setTarifaPorHora(tarifaPorHora);
        setHorasTrabajadas(horasTrabajadas);
    }

    // ── Implementación de métodos abstractos ───────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Calcula: horas ordinarias × tarifa + horas extras × (tarifa × factor extra).</p>
     *
     * @return salario bruto calculado según horas trabajadas
     * @throws SalarioNegativoException si el resultado es negativo (no debería ocurrir)
     */
    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        double horasOrdinarias = Math.min(horasTrabajadas, HORAS_JORNADA_ORDINARIA);
        double horasExtras     = Math.max(0, horasTrabajadas - HORAS_JORNADA_ORDINARIA);

        double pagoOrdinario = horasOrdinarias * tarifaPorHora;
        double pagoExtra     = horasExtras     * tarifaPorHora * FACTOR_HORA_EXTRA;
        double bruto         = pagoOrdinario + pagoExtra;

        if (bruto < 0) {
            throw new SalarioNegativoException(
                    "Salario bruto negativo para empleado: " + getNombre(), bruto);
        }
        return bruto;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Beneficio: auxilio de transporte si el salario bruto no supera
     * {@value #UMBRAL_AUXILIO}.</p>
     *
     * @return monto de auxilio de transporte (0 si no aplica)
     */
    @Override
    public double calcularBeneficios() {
        try {
            double bruto = calcularSalarioBruto();
            return (bruto < UMBRAL_AUXILIO) ? AUXILIO_TRANSPORTE : 0.0;
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Deducciones: aporte a salud + pensión sobre el salario bruto.</p>
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

    // ── Getters y Setters con validación ───────────────────────────────────

    /**
     * Retorna la tarifa de pago por hora ordinaria.
     *
     * @return tarifa por hora
     */
    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    /**
     * Establece la tarifa de pago por hora ordinaria.
     *
     * @param tarifaPorHora nueva tarifa (≥ 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public final void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora < 0) {
            throw new IllegalArgumentException(
                    "La tarifa por hora no puede ser negativa: " + tarifaPorHora);
        }
        this.tarifaPorHora = tarifaPorHora;
    }

    /**
     * Retorna las horas trabajadas en el período actual.
     *
     * @return horas trabajadas
     */
    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    /**
     * Establece las horas trabajadas en el período.
     *
     * @param horasTrabajadas horas trabajadas (0 ≤ valor ≤ {@value #HORAS_MAXIMAS_MENSUAL})
     * @throws HorasInvalidasException si el valor está fuera del rango válido
     */
    public final void setHorasTrabajadas(double horasTrabajadas)
            throws HorasInvalidasException {
        if (horasTrabajadas < 0 || horasTrabajadas > HORAS_MAXIMAS_MENSUAL) {
            throw new HorasInvalidasException(
                    "Horas inválidas para empleado: " + getNombre(),
                    horasTrabajadas,
                    HORAS_MAXIMAS_MENSUAL);
        }
        this.horasTrabajadas = horasTrabajadas;
    }

    /**
     * Representación textual del empleado por horas.
     *
     * @return cadena con datos básicos, tarifa y horas trabajadas
     */
    @Override
    public String toString() {
        return super.toString() + String.format(
                ", tarifaPorHora=%.2f, horasTrabajadas=%.2f",
                tarifaPorHora, horasTrabajadas);
    }
}
