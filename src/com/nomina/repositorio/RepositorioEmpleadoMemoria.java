package com.nomina.repositorio;

import com.nomina.modelo.Empleado;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio que guarda los empleados en memoria RAM.
 * Usa LinkedHashMap para mantener el orden en que se fueron agregando
 * y para buscar rapido por id. Sirve para desarollo y pruebas.
 */
public class RepositorioEmpleadoMemoria implements RepositorioEmpleado<Empleado> {

    private final Map<String, Empleado> almacen;

    public RepositorioEmpleadoMemoria() {
        this.almacen = new LinkedHashMap<>();
    }

    @Override
    public void guardar(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("No se puede guardar un empleado nulo.");
        }
        almacen.put(empleado.getId(), empleado);
    }

    @Override
    public Optional<Empleado> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(almacen.get(id.trim()));
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(almacen.values()));
    }

    @Override
    public boolean eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return almacen.remove(id.trim()) != null;
    }

    @Override
    public boolean existe(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return almacen.containsKey(id.trim());
    }

    @Override
    public int contarEmpleados() {
        return almacen.size();
    }

    @Override
    public String toString() {
        return "RepositorioEmpleadoMemoria{empleados=" + almacen.size() + "}";
    }
}
