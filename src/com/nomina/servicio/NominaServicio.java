package com.nomina.servicio;

import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.modelo.Empleado;
import com.nomina.modelo.TipoEmpleado;
import com.nomina.repositorio.RepositorioEmpleado;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Logica de negocio de la nomina.
 * Coordina las operaciones entre el repositorio (datos)
 * y los modelos (calculos). No guarda nada ni imprime nada.
 */
public class NominaServicio {

    private final RepositorioEmpleado<Empleado> repositorio;

    public NominaServicio(RepositorioEmpleado<Empleado> repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo.");
        }
        this.repositorio = repositorio;
    }

    // ── Gestion de empleados ───────────────────────────────

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

    public boolean eliminarEmpleado(String id) {
        return repositorio.eliminar(id);
    }

    public Optional<Empleado> buscarEmpleado(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Empleado> obtenerTodosLosEmpleados() {
        return repositorio.obtenerTodos();
    }

    public int contarEmpleados() {
        return repositorio.contarEmpleados();
    }

    // ── Calculos de nomina ─────────────────────────────────

    public double calcularSalarioNeto(String id) throws SalarioNegativoException {
        Empleado empleado = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empleado no encontrado con id: " + id));
        return empleado.calcularSalarioNeto();
    }

    /**
     * Arma el resumen completo de un empleado: sueldo bruto,
     * beneficios, deducciones y el neto final.
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
     * Genera la nomina de todos los empleados.
     * Si alguien falla, lo salta y avisa por consola de errores.
     */
    public List<ResumenNomina> generarNominaCompleta() {
        List<ResumenNomina> nomina = new ArrayList<>();
        for (Empleado emp : repositorio.obtenerTodos()) {
            try {
                nomina.add(generarResumenNomina(emp.getId()));
            } catch (SalarioNegativoException | IllegalArgumentException e) {
                System.err.println("[ERROR] No se pudo calcular nomina de "
                        + emp.getNombre() + ": " + e.getMessage());
            }
        }
        return nomina;
    }

    /** Suma de todos los sueldos netos de la empresa. */
    public double calcularTotalNomina() {
        return generarNominaCompleta().stream()
                .mapToDouble(ResumenNomina::getSalarioNeto)
                .sum();
    }

    /** Filtra empleados por tipo de contratacion. */
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
