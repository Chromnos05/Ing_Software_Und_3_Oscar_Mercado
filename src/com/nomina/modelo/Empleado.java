package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Clase base de todos los empleados.
 * Define lo basico: id, nombre, fecha de ingreso y tipo de contratacion.
 * Delega el calculo del sueldo a cada subclase.
 */
public abstract class Empleado {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String id;
    private String nombre;
    private LocalDate fechaIngreso;
    private TipoEmpleado tipoEmpleado;

    protected Empleado(String id,
                       String nombre,
                       LocalDate fechaIngreso,
                       TipoEmpleado tipoEmpleado) {
        setId(id);
        setNombre(nombre);
        setFechaIngreso(fechaIngreso);
        setTipoEmpleado(tipoEmpleado);
    }

    /**
     * Cuanto gana el empleado antes de beneficios y descuentos.
     */
    public abstract double calcularSalarioBruto() throws SalarioNegativoException;

    /**
     * Bonos, auxilios, primas... lo extra que recibe el empleado.
     */
    public abstract double calcularBeneficios();

    /**
     * Descuentos: salud, pension, retencion, ARL, etc.
     */
    public abstract double calcularDeducciones();

    /**
     * Lo que realmente llega al bolsillo del empleado:
     * bruto + beneficios - deducciones.
     */
    public double calcularSalarioNeto() throws SalarioNegativoException {
        double bruto       = calcularSalarioBruto();
        double beneficios  = calcularBeneficios();
        double deducciones = calcularDeducciones();
        double neto        = bruto + beneficios - deducciones;

        if (neto < 0) {
            throw new SalarioNegativoException(
                    "El salario neto del empleado '" + nombre + "' es negativo.", neto);
        }
        return neto;
    }

    // ── Getters y Setters ─────────────────────────────────

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo ni vacio.");
        }
        this.id = id.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado no puede ser nulo ni vacio.");
        }
        this.nombre = nombre.trim();
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public final void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula.");
        }
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de ingreso no puede ser futura: " + fechaIngreso);
        }
        this.fechaIngreso = fechaIngreso;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public final void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        if (tipoEmpleado == null) {
            throw new IllegalArgumentException("El tipo de empleado no puede ser nulo.");
        }
        this.tipoEmpleado = tipoEmpleado;
    }

    // ── Metodos de Object ─────────────────────────────────

    @Override
    public String toString() {
        return String.format("Empleado{id='%s', nombre='%s', tipo=%s, ingreso=%s}",
                id, nombre, tipoEmpleado.getDescripcion(),
                fechaIngreso.format(FORMATO_FECHA));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado other = (Empleado) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
