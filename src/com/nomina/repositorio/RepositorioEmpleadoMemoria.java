package com.nomina.repositorio;

import com.nomina.modelo.Empleado;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementacion en memoria del repositorio de empleados.
 *
 * <p>Utiliza un {@link LinkedHashMap} para mantener el orden de insercion
 * y garantizar acceso O(1) por id. Esta implementacion es adecuada para
 * desarrollo, pruebas y sistemas de pequeña escala.</p>
 *
 * <p>Principio SRP: esta clase es responsable unicamente de la
 * persistencia temporal de empleados en memoria RAM.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 * @see RepositorioEmpleado
 */
public class RepositorioEmpleadoMemoria implements RepositorioEmpleado<Empleado> {

    /** Mapa interno que almacena los empleados indexados por su id. */
    private final Map<String, Empleado> almacen;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Construye un repositorio en memoria vacio.
     */
    public RepositorioEmpleadoMemoria() {
        this.almacen = new LinkedHashMap<>();
    }

    // ── Operaciones CRUD ───────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Si el id ya existe, el empleado es reemplazado (update).</p>
     *
     * @throws IllegalArgumentException si el empleado es nulo
     */
    @Override
    public void guardar(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("No se puede guardar un empleado nulo.");
        }
        almacen.put(empleado.getId(), empleado);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Empleado> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(almacen.get(id.trim()));
    }

    /**
     * {@inheritDoc}
     *
     * @return lista no modificable de todos los empleados en orden de insercion
     */
    @Override
    public List<Empleado> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(almacen.values()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return almacen.remove(id.trim()) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existe(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return almacen.containsKey(id.trim());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int contarEmpleados() {
        return almacen.size();
    }

    /**
     * Retorna una representacion resumida del repositorio.
     *
     * @return cadena con la cantidad de empleados almacenados
     */
    @Override
    public String toString() {
        return "RepositorioEmpleadoMemoria{empleados=" + almacen.size() + "}";
    }
}

