package com.nomina;

import com.nomina.excepcion.HorasInvalidasException;
import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.excepcion.VentasInvalidasException;
import com.nomina.modelo.EmpleadoAsalariado;
import com.nomina.modelo.EmpleadoComision;
import com.nomina.modelo.EmpleadoPorHoras;
import com.nomina.modelo.EmpleadoTemporal;
import com.nomina.repositorio.RepositorioEmpleadoMemoria;
import com.nomina.servicio.NominaServicio;
import com.nomina.servicio.ResumenNomina;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase principal del Sistema de Nómina.
 *
 * <p>Punto de entrada de la aplicación. Demuestra el uso de todas las
 * clases del sistema: creación de empleados de distintos tipos, registro
 * en el repositorio, cálculo de nómina a través del servicio y presentación
 * de resultados en consola.</p>
 *
 * <p>Principio SRP: esta clase es responsable únicamente de orquestar
 * la demostración del sistema; no contiene lógica de negocio ni de acceso
 * a datos.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public class Main {

    /**
     * Constructor privado para evitar instanciación de la clase utilitaria.
     */
    private Main() { }

    /**
     * Método principal de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE NÓMINA - v1.0           ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // ── 1. Inicializar infraestructura ─────────────────────────────────
        RepositorioEmpleadoMemoria repositorio = new RepositorioEmpleadoMemoria();
        NominaServicio servicio = new NominaServicio(repositorio);

        // ── 2. Crear y registrar empleados ─────────────────────────────────
        registrarEmpleadosDemostracion(servicio);

        // ── 3. Generar y mostrar nómina completa ───────────────────────────
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  NÓMINA DEL MES - " + LocalDate.now().getMonth()
                + " " + LocalDate.now().getYear());
        System.out.println("═══════════════════════════════════════════════");
        System.out.println();

        List<ResumenNomina> nomina = servicio.generarNominaCompleta();
        for (ResumenNomina resumen : nomina) {
            System.out.println(resumen);
            System.out.println();
        }

        // ── 4. Mostrar totales ─────────────────────────────────────────────
        System.out.printf("  Total empleados : %d%n", servicio.contarEmpleados());
        System.out.printf("  Total nómina    : $ %,.2f%n%n", servicio.calcularTotalNomina());

        // ── 5. Demostración de excepciones personalizadas ──────────────────
        demostrarExcepciones();
    }

    /**
     * Crea y registra empleados de ejemplo de los cuatro tipos disponibles.
     *
     * @param servicio servicio de nómina donde se registran los empleados
     */
    private static void registrarEmpleadosDemostracion(NominaServicio servicio) {
        try {
            // Empleado asalariado
            EmpleadoAsalariado asalariado = new EmpleadoAsalariado(
                    "E001", "Ana García",
                    LocalDate.of(2021, 3, 15),
                    3_500_000.0);
            servicio.registrarEmpleado(asalariado);

            // Empleado por horas (con horas extras)
            EmpleadoPorHoras porHoras = new EmpleadoPorHoras(
                    "E002", "Carlos Mendoza",
                    LocalDate.of(2022, 7, 1),
                    25_000.0, 175.0);
            servicio.registrarEmpleado(porHoras);

            // Empleado por comisión (supera la meta de ventas)
            EmpleadoComision comision = new EmpleadoComision(
                    "E003", "Laura Ríos",
                    LocalDate.of(2020, 1, 10),
                    1_500_000.0, 0.05, 15_000_000.0);
            servicio.registrarEmpleado(comision);

            // Empleado temporal
            EmpleadoTemporal temporal = new EmpleadoTemporal(
                    "E004", "Pedro Castillo",
                    LocalDate.of(2025, 12, 1),
                    85_000.0, 22,
                    LocalDate.of(2026, 6, 30));
            servicio.registrarEmpleado(temporal);

            System.out.println("✔ " + servicio.contarEmpleados()
                    + " empleados registrados correctamente.");
            System.out.println();

        } catch (HorasInvalidasException | VentasInvalidasException e) {
            System.err.println("[ERROR] Al crear empleado: " + e.getMessage());
        }
    }

    /**
     * Demuestra el lanzamiento y captura de las excepciones personalizadas
     * del sistema.
     */
    private static void demostrarExcepciones() {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  DEMO DE EXCEPCIONES PERSONALIZADAS");
        System.out.println("═══════════════════════════════════════════════");

        // SalarioNegativoException
        try {
            throw new SalarioNegativoException("Prueba de salario negativo", -500.0);
        } catch (SalarioNegativoException e) {
            System.out.println("✔ SalarioNegativoException capturada: " + e.getMessage());
        }

        // HorasInvalidasException
        try {
            throw new HorasInvalidasException("Horas fuera de rango", 250.0, 192.0);
        } catch (HorasInvalidasException e) {
            System.out.println("✔ HorasInvalidasException capturada  : " + e.getMessage());
        }

        // VentasInvalidasException
        try {
            throw new VentasInvalidasException("Ventas negativas", -1000.0);
        } catch (VentasInvalidasException e) {
            System.out.println("✔ VentasInvalidasException capturada : " + e.getMessage());
        }

        // IllegalArgumentException - empleado con id vacío
        try {
            new EmpleadoAsalariado("", "Test", LocalDate.now().minusDays(1), 1000.0);
        } catch (IllegalArgumentException e) {
            System.out.println("✔ IllegalArgumentException capturada : " + e.getMessage());
        }

        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         Ejecución completada ✔           ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
