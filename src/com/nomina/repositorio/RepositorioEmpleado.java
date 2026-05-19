package com.nomina.repositorio;

import com.nomina.modelo.Empleado;
import java.util.List;
import java.util.Optional;

/**
 * Contrato que define las operaciones CRUD básicas para el repositorio de empleados.
 *
 * <p>Cualquier implementación de este repositorio (en memoria, base de datos,
 * archivo, etc.) debe satisfacer esta interfaz, lo que permite intercambiar
 * la fuente de datos sin modificar las capas superiores (servicio, presentación).</p>
 *
 * <p>Principio SRP: esta interfaz es responsable únicamente de definir
 * el contrato de acceso a datos de empleados.</p>
 *
 * @param <T> tipo concreto de {@link Empleado} que gestiona el repositorio
 * @author Sistema Nómina
 * @version 1.0
 */
public interface RepositorioEmpleado<T extends Empleado> {

    /**
     * Guarda un empleado en el repositorio.
     *
     * <p>Si el empleado ya existe (mismo id), su información es actualizada.</p>
     *
     * @param empleado empleado a guardar (no nulo)
     * @throws IllegalArgumentException si el empleado es nulo
     */
    void guardar(T empleado);

    /**
     * Busca un empleado por su identificador único.
     *
     * @param id identificador del empleado
     * @return {@link Optional} con el empleado encontrado, o vacío si no existe
     */
    Optional<T> buscarPorId(String id);

    /**
     * Retorna todos los empleados almacenados en el repositorio.
     *
     * @return lista no modificable de todos los empleados
     */
    List<T> obtenerTodos();

    /**
     * Elimina el empleado con el id indicado del repositorio.
     *
     * @param id identificador del empleado a eliminar
     * @return {@code true} si el empleado existía y fue eliminado,
     *         {@code false} si no se encontró
     */
    boolean eliminar(String id);

    /**
     * Indica si existe un empleado con el id dado en el repositorio.
     *
     * @param id identificador a verificar
     * @return {@code true} si el empleado existe
     */
    boolean existe(String id);

    /**
     * Retorna el número de empleados almacenados.
     *
     * @return cantidad de empleados en el repositorio
     */
    int contarEmpleados();
}
