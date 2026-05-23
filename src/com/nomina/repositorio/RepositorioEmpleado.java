package com.nomina.repositorio;

import com.nomina.modelo.Empleado;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para el almacenamiento de empleados.
 * Quien implemente esto (memoria, base de datos, archivo)
 * se compromete a ofrecer estas operaciones basicas.
 */
public interface RepositorioEmpleado<T extends Empleado> {

    void guardar(T empleado);

    Optional<T> buscarPorId(String id);

    List<T> obtenerTodos();

    boolean eliminar(String id);

    boolean existe(String id);

    int contarEmpleados();
}
