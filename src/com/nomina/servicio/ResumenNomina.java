package com.nomina.servicio;

import com.nomina.modelo.Empleado;

/**
 * Resultado del calculo de nomina de un empleado.
 * Es solo un envoltorio para mostrar los datos: empleado,
 * sueldo bruto, beneficios, deducciones y neto.
 * No se puede modificar una vez creado.
 */
public class ResumenNomina {

    private final Empleado empleado;
    private final double salarioBruto;
    private final double beneficios;
    private final double deducciones;
    private final double salarioNeto;

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

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public double getBeneficios() {
        return beneficios;
    }

    public double getDeducciones() {
        return deducciones;
    }

    public double getSalarioNeto() {
        return salarioNeto;
    }

    /**
     * Comprobante de nomina listo para imprimir en consola.
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
