package com.nomina.modelo;

import com.nomina.excepcion.HorasInvalidasException;
import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Empleado que se paga por hora trabajada.
 * Si hace horas extra (mas de 160 al mes), se las pagan con recargo del 50%.
 * Si gana poco, recibe auxilio de transporte.
 */
public class EmpleadoPorHoras extends Empleado {

    public static final double HORAS_MAXIMAS_MENSUAL    = 192.0;
    public static final double HORAS_JORNADA_ORDINARIA  = 160.0;
    public static final double FACTOR_HORA_EXTRA        = 1.50;

    private static final double PORCENTAJE_SALUD        = 0.04;
    private static final double PORCENTAJE_PENSION      = 0.04;
    private static final double AUXILIO_TRANSPORTE      = 117172.0;
    private static final double UMBRAL_AUXILIO          = 2_600_000.0;

    private double tarifaPorHora;
    private double horasTrabajadas;

    public EmpleadoPorHoras(String id,
                            String nombre,
                            LocalDate fechaIngreso,
                            double tarifaPorHora,
                            double horasTrabajadas) throws HorasInvalidasException {
        super(id, nombre, fechaIngreso, TipoEmpleado.POR_HORAS);
        setTarifaPorHora(tarifaPorHora);
        setHorasTrabajadas(horasTrabajadas);
    }

    /**
     * Las primeras 160 horas se pagan a tarifa normal.
     * Lo que exceda de 160 se cuenta como hora extra con recargo del 50%.
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

    /** Auxilio de transporte (~$117,172) si el sueldo bruto es menor a $2,600,000. */
    @Override
    public double calcularBeneficios() {
        try {
            double bruto = calcularSalarioBruto();
            return (bruto < UMBRAL_AUXILIO) ? AUXILIO_TRANSPORTE : 0.0;
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
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

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public final void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora < 0) {
            throw new IllegalArgumentException(
                    "La tarifa por hora no puede ser negativa: " + tarifaPorHora);
        }
        this.tarifaPorHora = tarifaPorHora;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public final void setHorasTrabajadas(double horasTrabajadas)
            throws HorasInvalidasException {
        if (horasTrabajadas < 0 || horasTrabajadas > HORAS_MAXIMAS_MENSUAL) {
            throw new HorasInvalidasException(
                    "Horas invalidas para empleado: " + getNombre(),
                    horasTrabajadas,
                    HORAS_MAXIMAS_MENSUAL);
        }
        this.horasTrabajadas = horasTrabajadas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", tarifaPorHora=%.2f, horasTrabajadas=%.2f",
                tarifaPorHora, horasTrabajadas);
    }
}
