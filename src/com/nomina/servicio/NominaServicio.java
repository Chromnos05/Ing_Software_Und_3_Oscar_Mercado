package com.nomina.servicio;

import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.modelo.Empleado;
import com.nomina.modelo.TipoEmpleado;
import com.nomina.repositorio.RepositorioEmpleado;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de nómina que encapsula la lógica de negocio relacionada
 * con el cálculo y gestión de salarios de los empleados.
 *
 * <p>Actúa como intermediario entre la capa de presentación/control y la
 * capa de repositorio, orquestando las operaciones de cálculo salarial
 * y aplicando reglas de negocio transversales.</p>
 *
 * <p>Principio SRP: esta clase es responsable únicamente de la lógica
 * de negocio de la nómina. No persiste datos (eso lo hace el repositorio)
 * ni presenta resultados al usuario (eso lo hace la clase Main o un
 * controlador).</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 * @see RepositorioEmpleado
 * @see Empleado
 */
public class NominaServicio {

    /** Repositorio que provee el acceso a los datos de empleados. */
    private final RepositorioEmpleado<Empleado> repositorio;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye el servicio de nómina con el repositorio indicado.
     *
     * @param repositorio repositorio de empleados (no nulo)
     * @throws IllegalArgumentException si el repositorio es nulo
     */
    public NominaServicio(RepositorioEmpleado<Empleado> repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo.");
        }
        this.repositorio = repositorio;
    }

    // ── Gestión de empleados ───────────────────────────────────────────────

    /**
     * Registra un nuevo empleado en el sistema.
     *
     * @param empleado empleado a registrar (no nulo)
     * @throws IllegalArgumentException si ya existe un empleado con el mismo id
     */
    public void registrarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado a registrar no puede ser nulo.");
        }
        if (repositorio.existe(empleado.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un empleado con el id: " + empleado.getId());
        }
        repositorio.guardar(empleado);
    }

    /**
     * Actualiza los datos de un empleado existente.
     *
     * @param empleado empleado con datos actualizados (no nulo)
     * @throws IllegalArgumentException si el empleado no existe en el sistema
     */
    public void actualizarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado a actualizar no puede ser nulo.");
        }
        if (!repositorio.existe(empleado.getId())) {
            throw new IllegalArgumentException(
                    "No existe empleado con el id: " + empleado.getId());
        }
        repositorio.guardar(empleado);
    }

    /**
     * Elimina un empleado del sistema por su id.
     *
     * @param id identificador del empleado a eliminar
     * @return {@code true} si fue eliminado, {@code false} si no existía
     */
    public boolean eliminarEmpleado(String id) {
        return repositorio.eliminar(id);
    }

    /**
     * Busca un empleado por su identificador único.
     *
     * @param id identificador del empleado
     * @return {@link Optional} con el empleado, o vacío si no existe
     */
    public Optional<Empleado> buscarEmpleado(String id) {
        return repositorio.buscarPorId(id);
    }

    /**
     * Retorna la lista de todos los empleados registrados.
     *
     * @return lista de todos los empleados
     */
    public List<Empleado> obtenerTodosLosEmpleados() {
        return repositorio.obtenerTodos();
    }

    /**
     * Retorna el total de empleados registrados en el sistema.
     *
     * @return cantidad de empleados
     */
    public int contarEmpleados() {
        return repositorio.contarEmpleados();
    }

    // ── Cálculos de nómina ─────────────────────────────────────────────────

    /**
     * Calcula el salario neto de un empleado específico.
     *
     * @param id identificador del empleado
     * @return salario neto calculado
     * @throws IllegalArgumentException si no existe el empleado con ese id
     * @throws SalarioNegativoException si el cálculo arroja un resultado negativo
     */
    public double calcularSalarioNeto(String id) throws SalarioNegativoException {
        Empleado empleado = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empleado no encontrado con id: " + id));
        return empleado.calcularSalarioNeto();
    }

    /**
     * Genera el resumen de nómina de un empleado específico.
     *
     * @param id identificador del empleado
     * @return objeto {@link ResumenNomina} con todos los valores calculados
     * @throws IllegalArgumentException si no existe el empleado con ese id
     * @throws SalarioNegativoException si algún cálculo es negativo
     */
    public ResumenNomina generarResumenNomina(String id) throws SalarioNegativoException {
        Empleado empleado = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empleado no encontrado con id: " + id));

        double bruto       = empleado.calcularSalarioBruto();
        double beneficios  = empleado.calcularBeneficios();
        double deducciones = empleado.calcularDeducciones();
        double neto        = empleado.calcularSalarioNeto();

        return new ResumenNomina(empleado, bruto, beneficios, deducciones, neto);
    }

    /**
     * Genera los resúmenes de nómina de todos los empleados registrados.
     *
     * <p>Los empleados que produzcan un error de cálculo son omitidos y
     * el error es registrado en la consola de error estándar.</p>
     *
     * @return lista de resúmenes de nómina
     */
    public List<ResumenNomina> generarNominaCompleta() {
        List<ResumenNomina> nomina = new ArrayList<>();
        for (Empleado emp : repositorio.obtenerTodos()) {
            try {
                nomina.add(generarResumenNomina(emp.getId()));
            } catch (SalarioNegativoException | IllegalArgumentException e) {
                System.err.println("[ERROR] No se pudo calcular nómina de "
                        + emp.getNombre() + ": " + e.getMessage());
            }
        }
        return nomina;
    }

    /**
     * Calcula el total de la nómina (suma de todos los salarios netos).
     *
     * @return suma total de salarios netos de todos los empleados
     */
    public double calcularTotalNomina() {
        return generarNominaCompleta().stream()
                .mapToDouble(ResumenNomina::getSalarioNeto)
                .sum();
    }

    /**
     * Filtra los empleados por tipo contractual.
     *
     * @param tipo tipo de empleado a filtrar
     * @return lista de empleados que coinciden con el tipo indicado
     */
    public List<Empleado> filtrarPorTipo(TipoEmpleado tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de empleado no puede ser nulo.");
        }
        List<Empleado> resultado = new ArrayList<>();
        for (Empleado emp : repositorio.obtenerTodos()) {
            if (emp.getTipoEmpleado() == tipo) {
                resultado.add(emp);
            }
        }
        return resultado;
    }
}
