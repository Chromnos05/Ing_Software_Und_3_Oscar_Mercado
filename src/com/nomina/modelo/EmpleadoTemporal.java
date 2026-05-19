package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Representa a un empleado temporal contratado por un período definido.
 *
 * <p>Recibe un pago diario fijo por los días efectivamente trabajados.
 * Al ser temporal, no tiene derecho a prima de servicios ni a los
 * beneficios de largo plazo propios de los empleados indefinidos.</p>
 *
 * <p>Principio SRP: esta clase es responsable únicamente de modelar
 * la lógica salarial del empleado temporal.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 * @see Empleado
 */
public class EmpleadoTemporal extends Empleado {

    // ── Constantes ─────────────────────────────────────────────────────────

    /** Número máximo de días laborales en un mes (incluye posibles días extra). */
    public static final int DIAS_MAX_MENSUAL = 31;

    /** Porcentaje de retención sobre el pago diario (retefuente simplificada 1 %). */
    private static final double PORCENTAJE_RETENCION = 0.01;

    /** Porcentaje de aporte a riesgos laborales ARL (0.522 %). */
    private static final double PORCENTAJE_ARL       = 0.00522;

    // ── Atributos privados ─────────────────────────────────────────────────

    /** Tarifa de pago por día trabajado (≥ 0). */
    private double pagoDiario;

    /** Número de días trabajados en el período (1 ≤ valor ≤ DIAS_MAX_MENSUAL). */
    private int diasTrabajados;

    /** Fecha de finalización del contrato temporal. */
    private LocalDate fechaFinContrato;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un empleado temporal.
     *
     * @param id               identificador único
     * @param nombre           nombre completo
     * @param fechaIngreso     fecha de inicio del contrato
     * @param pagoDiario       tarifa de pago por día (≥ 0)
     * @param diasTrabajados   días trabajados en el período (1 a {@value #DIAS_MAX_MENSUAL})
     * @param fechaFinContrato fecha de finalización del contrato (posterior a fechaIngreso)
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    public EmpleadoTemporal(String id,
                            String nombre,
                            LocalDate fechaIngreso,
                            double pagoDiario,
                            int diasTrabajados,
                            LocalDate fechaFinContrato) {
        super(id, nombre, fechaIngreso, TipoEmpleado.TEMPORAL);
        setPagoDiario(pagoDiario);
        setDiasTrabajados(diasTrabajados);
        setFechaFinContrato(fechaFinContrato, fechaIngreso);
    }

    // ── Implementación de métodos abstractos ───────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Fórmula: {@code salarioBruto = pagoDiario × diasTrabajados}</p>
     *
     * @return salario bruto según días trabajados
     * @throws SalarioNegativoException si el resultado es negativo (no debería ocurrir)
     */
    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        double bruto = pagoDiario * diasTrabajados;
        if (bruto < 0) {
            throw new SalarioNegativoException(
                    "Salario bruto negativo para empleado temporal: " + getNombre(), bruto);
        }
        return bruto;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Los empleados temporales no tienen beneficios adicionales; retorna 0.</p>
     *
     * @return 0.0 siempre
     */
    @Override
    public double calcularBeneficios() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Deducciones: retención en la fuente ({@value #PORCENTAJE_RETENCION})
     * + ARL ({@value #PORCENTAJE_ARL}) sobre el salario bruto.</p>
     *
     * @return total de deducciones aplicadas al empleado temporal
     */
    @Override
    public double calcularDeducciones() {
        try {
            return calcularSalarioBruto() * (PORCENTAJE_RETENCION + PORCENTAJE_ARL);
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
    }

    // ── Getters y Setters con validación ───────────────────────────────────

    /**
     * Retorna la tarifa de pago por día trabajado.
     *
     * @return pago diario
     */
    public double getPagoDiario() {
        return pagoDiario;
    }

    /**
     * Establece la tarifa de pago por día trabajado.
     *
     * @param pagoDiario nueva tarifa diaria (≥ 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public final void setPagoDiario(double pagoDiario) {
        if (pagoDiario < 0) {
            throw new IllegalArgumentException(
                    "El pago diario no puede ser negativo: " + pagoDiario);
        }
        this.pagoDiario = pagoDiario;
    }

    /**
     * Retorna los días trabajados en el período actual.
     *
     * @return días trabajados
     */
    public int getDiasTrabajados() {
        return diasTrabajados;
    }

    /**
     * Establece los días trabajados en el período.
     *
     * @param diasTrabajados días trabajados (1 ≤ valor ≤ {@value #DIAS_MAX_MENSUAL})
     * @throws IllegalArgumentException si el valor está fuera del rango válido
     */
    public final void setDiasTrabajados(int diasTrabajados) {
        if (diasTrabajados < 1 || diasTrabajados > DIAS_MAX_MENSUAL) {
            throw new IllegalArgumentException(
                    "Días trabajados inválidos (" + diasTrabajados + "). "
                    + "Rango permitido: 1 – " + DIAS_MAX_MENSUAL);
        }
        this.diasTrabajados = diasTrabajados;
    }

    /**
     * Retorna la fecha de finalización del contrato temporal.
     *
     * @return fecha fin de contrato
     */
    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    /**
     * Establece la fecha de finalización del contrato.
     *
     * @param fechaFinContrato fecha de fin (no nula, posterior a fechaIngreso)
     * @param fechaIngreso     fecha de inicio del contrato para validar orden cronológico
     * @throws IllegalArgumentException si la fecha fin es nula o anterior a la de ingreso
     */
    public final void setFechaFinContrato(LocalDate fechaFinContrato,
                                          LocalDate fechaIngreso) {
        if (fechaFinContrato == null) {
            throw new IllegalArgumentException("La fecha de fin de contrato no puede ser nula.");
        }
        if (!fechaFinContrato.isAfter(fechaIngreso)) {
            throw new IllegalArgumentException(
                    "La fecha de fin de contrato debe ser posterior a la de ingreso. "
                    + "Ingreso: " + fechaIngreso + " | Fin: " + fechaFinContrato);
        }
        this.fechaFinContrato = fechaFinContrato;
    }

    /**
     * Representación textual del empleado temporal.
     *
     * @return cadena con datos básicos, pago diario, días trabajados y fecha fin
     */
    @Override
    public String toString() {
        return super.toString() + String.format(
                ", pagoDiario=%.2f, diasTrabajados=%d, finContrato=%s",
                pagoDiario, diasTrabajados, fechaFinContrato);
    }
}
