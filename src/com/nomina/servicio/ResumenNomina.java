package com.nomina.servicio;

import com.nomina.modelo.Empleado;

/**
 * DTO (Data Transfer Object) que agrupa los valores calculados de la nómina
 * de un empleado en un período determinado.
 *
 * <p>Encapsula de forma inmutable el resultado del cálculo salarial para que
 * pueda ser transportado entre capas (servicio → presentación) sin exponer
 * la lógica interna de los modelos.</p>
 *
 * <p>Principio SRP: esta clase es responsable únicamente de transportar
 * el resultado del cálculo de nómina; no realiza cálculos ni accede a
 * ninguna fuente de datos.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 * @see NominaServicio
 */
public class ResumenNomina {

    // ── Atributos (inmutables) ─────────────────────────────────────────────

    /** Empleado al que pertenece este resumen. */
    private final Empleado empleado;

    /** Salario bruto calculado. */
    private final double salarioBruto;

    /** Total de beneficios adicionales. */
    private final double beneficios;

    /** Total de deducciones aplicadas. */
    private final double deducciones;

    /** Salario neto final (bruto + beneficios - deducciones). */
    private final double salarioNeto;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un resumen de nómina con todos los valores calculados.
     *
     * @param empleado    empleado al que corresponde el resumen (no nulo)
     * @param salarioBruto salario bruto del período
     * @param beneficios  total de beneficios del período
     * @param deducciones total de deducciones del período
     * @param salarioNeto salario neto resultante
     * @throws IllegalArgumentException si el empleado es nulo
     */
    public ResumenNomina(Empleado empleado,
                         double salarioBruto,
                         double beneficios,
                         double deducciones,
                         double salarioNeto) {
        if (empleado == null) {
            throw new IllegalArgumentException(
                    "El empleado del resumen no puede ser nulo.");
        }
        this.empleado    = empleado;
        this.salarioBruto = salarioBruto;
        this.beneficios  = beneficios;
        this.deducciones = deducciones;
        this.salarioNeto = salarioNeto;
    }

    // ── Getters ────────────────────────────────────────────────────────────

    /**
     * Retorna el empleado al que pertenece este resumen.
     *
     * @return empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * Retorna el salario bruto calculado.
     *
     * @return salario bruto
     */
    public double getSalarioBruto() {
        return salarioBruto;
    }

    /**
     * Retorna el total de beneficios adicionales.
     *
     * @return beneficios
     */
    public double getBeneficios() {
        return beneficios;
    }

    /**
     * Retorna el total de deducciones aplicadas.
     *
     * @return deducciones
     */
    public double getDeducciones() {
        return deducciones;
    }

    /**
     * Retorna el salario neto calculado.
     *
     * @return salario neto
     */
    public double getSalarioNeto() {
        return salarioNeto;
    }

    // ── Representación textual ─────────────────────────────────────────────

    /**
     * Retorna una representación formateada del resumen de nómina, apta
     * para imprimirse en consola como comprobante básico.
     *
     * @return cadena con los datos del empleado y su nómina del período
     */
    @Override
    public String toString() {
        return String.format(
                "┌─────────────────────────────────────────┐%n"
                + "│  RESUMEN DE NÓMINA                      │%n"
                + "├─────────────────────────────────────────┤%n"
                + "│  Empleado  : %-27s│%n"
                + "│  ID        : %-27s│%n"
                + "│  Tipo      : %-27s│%n"
                + "├─────────────────────────────────────────┤%n"
                + "│  Bruto     : %,27.2f │%n"
                + "│  Beneficios: %,27.2f │%n"
                + "│  Deducciones:%,27.2f │%n"
                + "├─────────────────────────────────────────┤%n"
                + "│  NETO      : %,27.2f │%n"
                + "└─────────────────────────────────────────┘",
                empleado.getNombre(),
                empleado.getId(),
                empleado.getTipoEmpleado().getDescripcion(),
                salarioBruto,
                beneficios,
                deducciones,
                salarioNeto);
    }
}
