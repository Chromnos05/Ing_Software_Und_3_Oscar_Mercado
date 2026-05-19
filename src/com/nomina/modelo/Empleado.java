package com.nomina.modelo;

import com.nomina.excepcion.SalarioNegativoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Clase abstracta base del sistema de nomina que representa a un empleado generico.
 *
 * <p>Define la estructura comun a todos los tipos de empleados: atributos de
 * identificacion y fecha de ingreso, metodos abstractos de calculo salarial
 * que cada subclase debe implementar, y un metodo concreto para calcular el
 * salario neto a partir de los valores calculados.</p>
 *
 * <p>Principio SRP: esta clase es responsable <em>exclusivamente</em> de modelar
 * la identidad y la logica salarial base de un empleado. La persistencia, el
 * formateo de reportes y otras responsabilidades recaen en clases separadas.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 * @see TipoEmpleado
 * @see com.nomina.excepcion.SalarioNegativoException
 */
public abstract class Empleado {

    // ── Constantes ─────────────────────────────────────────────────────────

    /** Formato de fecha usado en la representacion textual. */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Atributos privados ─────────────────────────────────────────────────

    /** Identificador unico del empleado. No puede ser nulo ni vacio. */
    private String id;

    /** Nombre completo del empleado. No puede ser nulo ni vacio. */
    private String nombre;

    /** Fecha en que el empleado ingreso a la empresa. No puede ser nula ni futura. */
    private LocalDate fechaIngreso;

    /** Tipo contractual del empleado. */
    private TipoEmpleado tipoEmpleado;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un empleado con los datos basicos de identidad.
     *
     * @param id           identificador unico (no nulo ni vacio)
     * @param nombre       nombre completo (no nulo ni vacio)
     * @param fechaIngreso fecha de ingreso (no nula, no futura)
     * @param tipoEmpleado tipo de contratacion (no nulo)
     * @throws IllegalArgumentException si alguno de los parametros es invalido
     */
    protected Empleado(String id,
                       String nombre,
                       LocalDate fechaIngreso,
                       TipoEmpleado tipoEmpleado) {
        setId(id);
        setNombre(nombre);
        setFechaIngreso(fechaIngreso);
        setTipoEmpleado(tipoEmpleado);
    }

    // ── Metodos abstractos ─────────────────────────────────────────────────

    /**
     * Calcula el salario bruto del empleado segun su modalidad de contratacion.
     *
     * <p>El salario bruto es la remuneracion antes de aplicar beneficios
     * y deducciones.</p>
     *
     * @return salario bruto (valor ≥ 0)
     * @throws SalarioNegativoException si el calculo arroja un resultado negativo
     */
    public abstract double calcularSalarioBruto() throws SalarioNegativoException;

    /**
     * Calcula el total de beneficios adicionales al salario base.
     *
     * <p>Los beneficios incluyen bonos, auxilio de transporte, prima de
     * servicios u otras compensaciones positivas segun el tipo de empleado.</p>
     *
     * @return monto total de beneficios (valor ≥ 0)
     */
    public abstract double calcularBeneficios();

    /**
     * Calcula el total de deducciones aplicables al salario del empleado.
     *
     * <p>Las deducciones incluyen aportes a seguridad social, retencion en la
     * fuente, prestamos u otros descuentos segun el tipo de empleado.</p>
     *
     * @return monto total de deducciones (valor ≥ 0)
     */
    public abstract double calcularDeducciones();

    // ── Metodo concreto de calculo ─────────────────────────────────────────

    /**
     * Calcula el salario neto del empleado.
     *
     * <p>Formula: {@code salarioNeto = salarioBruto + beneficios - deducciones}</p>
     *
     * @return salario neto del empleado
     * @throws SalarioNegativoException si el salario neto resultante es negativo
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

    // ── Getters y Setters con validacion ───────────────────────────────────

    /**
     * Retorna el identificador unico del empleado.
     *
     * @return id del empleado
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador unico del empleado.
     *
     * @param id nuevo identificador (no nulo ni vacio)
     * @throws IllegalArgumentException si el id es nulo o vacio
     */
    public final void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo ni vacio.");
        }
        this.id = id.trim();
    }

    /**
     * Retorna el nombre completo del empleado.
     *
     * @return nombre del empleado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo del empleado.
     *
     * @param nombre nuevo nombre (no nulo ni vacio)
     * @throws IllegalArgumentException si el nombre es nulo o vacio
     */
    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado no puede ser nulo ni vacio.");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Retorna la fecha de ingreso del empleado a la empresa.
     *
     * @return fecha de ingreso
     */
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * Establece la fecha de ingreso del empleado.
     *
     * @param fechaIngreso fecha de ingreso (no nula, no posterior a hoy)
     * @throws IllegalArgumentException si la fecha es nula o futura
     */
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

    /**
     * Retorna el tipo contractual del empleado.
     *
     * @return tipo de empleado
     */
    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    /**
     * Establece el tipo contractual del empleado.
     *
     * @param tipoEmpleado tipo de empleado (no nulo)
     * @throws IllegalArgumentException si el tipo es nulo
     */
    public final void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        if (tipoEmpleado == null) {
            throw new IllegalArgumentException("El tipo de empleado no puede ser nulo.");
        }
        this.tipoEmpleado = tipoEmpleado;
    }

    // ── Metodos de Object ──────────────────────────────────────────────────

    /**
     * Retorna una representacion textual del empleado con sus datos principales.
     *
     * @return cadena con id, nombre, tipo y fecha de ingreso
     */
    @Override
    public String toString() {
        return String.format("Empleado{id='%s', nombre='%s', tipo=%s, ingreso=%s}",
                id, nombre, tipoEmpleado.getDescripcion(),
                fechaIngreso.format(FORMATO_FECHA));
    }

    /**
     * Compara dos empleados por su identificador unico.
     *
     * @param obj objeto a comparar
     * @return {@code true} si ambos empleados tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado other = (Empleado) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * Retorna el hash code basado en el identificador unico del empleado.
     *
     * @return hash code del empleado
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

