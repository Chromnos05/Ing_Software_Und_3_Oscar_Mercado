package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;

/**
 * Empleado temporal: contrato por un periodo definido.
 * Cobra por dia trabajado, no tiene prima ni prestaciones de largo plazo.
 * Solo se le descuenta retencion en la fuente y ARL.
 */
public class EmpleadoTemporal extends Empleado {

    public static final int DIAS_MAX_MENSUAL = 31;

    private static final double PORCENTAJE_RETENCION = 0.01;
    private static final double PORCENTAJE_ARL       = 0.00522;

    private double pagoDiario;
    private int diasTrabajados;
    private LocalDate fechaFinContrato;

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

    /** Pago diario multiplicado por los dias que trabajo en el mes. */
    @Override
    public double calcularSalarioBruto() throws SalarioNegativoException {
        double bruto = pagoDiario * diasTrabajados;
        if (bruto < 0) {
            throw new SalarioNegativoException(
                    "Salario bruto negativo para empleado temporal: " + getNombre(), bruto);
        }
        return bruto;
    }

    /** Los temporales no tienen bonos ni primas. */
    @Override
    public double calcularBeneficios() {
        return 0.0;
    }

    /** Retencion en la fuente (1%) + ARL (0.522%) sobre el bruto. */
    @Override
    public double calcularDeducciones() {
        try {
            return calcularSalarioBruto() * (PORCENTAJE_RETENCION + PORCENTAJE_ARL);
        } catch (SalarioNegativoException e) {
            return 0.0;
        }
    }

    public double getPagoDiario() {
        return pagoDiario;
    }

    public final void setPagoDiario(double pagoDiario) {
        if (pagoDiario < 0) {
            throw new IllegalArgumentException(
                    "El pago diario no puede ser negativo: " + pagoDiario);
        }
        this.pagoDiario = pagoDiario;
    }

    public int getDiasTrabajados() {
        return diasTrabajados;
    }

    public final void setDiasTrabajados(int diasTrabajados) {
        if (diasTrabajados < 1 || diasTrabajados > DIAS_MAX_MENSUAL) {
            throw new IllegalArgumentException(
                    "Dias trabajados invalidos (" + diasTrabajados + "). "
                    + "Rango permitido: 1 – " + DIAS_MAX_MENSUAL);
        }
        this.diasTrabajados = diasTrabajados;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

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

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", pagoDiario=%.2f, diasTrabajados=%d, finContrato=%s",
                pagoDiario, diasTrabajados, fechaFinContrato);
    }
}
