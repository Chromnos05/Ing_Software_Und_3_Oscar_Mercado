package com.nomina.servicio;

import com.nomina.modelo.Empleado;

/**
 * DTO (Data Transfer Object) que agrupa los valores calculados de la nomina
 * de un empleado en un periodo determinado.
 *
 * <p>Encapsula de forma inmutable el resultado del calculo salarial para que
 * pueda ser transportado entre capas (servicio → presentacion) sin exponer
 * la logica interna de los modelos.</p>
 *
 * <p>Principio SRP: esta clase es responsable unicamente de transportar
 * el resultado del calculo de nomina; no realiza calculos ni accede a
 * ninguna fuente de datos.</p>
 *
 * @author Sistema Nomina
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
     * Construye un resumen de nomina con todos los valores calculados.
     *
     * @param empleado    empleado al que corresponde el resumen (no nulo)
     * @param salarioBruto salario bruto del periodo
     * @param beneficios  total de beneficios del periodo
     * @param deducciones total de deducciones del periodo
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

    // ── Representacion textual ─────────────────────────────────────────────

    /**
     * Retorna una representacion formateada del resumen de nomina, apta
     * para imprimirse en consola como comprobante basico.
     *
     * @return cadena con los datos del empleado y su nomina del periodo
     */
    @Override
    public String toString() {
        return String.format(
                  "----------------------------------------------%n"
                + "*  RESUMEN DE NOMINA                          %n"
                + "----------------------------------------------%n"
                + "*  Empleado  : %-27s                          %n"
                + "*  ID        : %-27s                          %n"
                + "*  Tipo      : %-27s                          %n"
                + "----------------------------------------------%n"
                + "*  Bruto     : %,27.2f                        %n"
                + "*  Beneficios: %,27.2f                        %n"
                + "*  Deducciones:%,27.2f                        %n"
                + "----------------------------------------------%n"
                + "*  NETO      : %,27.2f                        %n"
                + "----------------------------------------------",
                empleado.getNombre(),
                empleado.getId(),
                empleado.getTipoEmpleado().getDescripcion(),
                salarioBruto,
                beneficios,
                deducciones,
                salarioNeto);
    }
}

