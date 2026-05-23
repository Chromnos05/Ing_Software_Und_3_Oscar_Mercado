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
 * Punto de entrada del sistema.
 * Crea empleados de ejemplo, calcula la nomina del mes
 * y muestra todo en consola para que se vea que funciona.
 */
public class Main {

    private Main() { }

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("*      SISTEMA DE NOMINA - v1.0      *");
        System.out.println("======================================");
        System.out.println();

        // ── 1. Preparar el sistema ──────────────────────────
        RepositorioEmpleadoMemoria repositorio = new RepositorioEmpleadoMemoria();
        NominaServicio servicio = new NominaServicio(repositorio);

        // ── 2. Crear empleados de prueba ─────────────────────
        registrarEmpleadosDemostracion(servicio);

        // ── 3. Calcular y mostrar la nomina del mes ──────────
        System.out.println("=========================================");
        System.out.println("* NOMINA DEL MES - " + LocalDate.now().getMonth()
                + " " + LocalDate.now().getYear());
        System.out.println("=========================================");
        System.out.println();

        List<ResumenNomina> nomina = servicio.generarNominaCompleta();
        for (ResumenNomina resumen : nomina) {
            System.out.println(resumen);
            System.out.println();
        }

        // ── 4. Totales ───────────────────────────────────────
        System.out.printf("  Total empleados : %d%n", servicio.contarEmpleados());
        System.out.printf("  Total nomina    : $ %,.2f%n%n", servicio.calcularTotalNomina());

        // ── 5. Probar las excepciones ────────────────────────
        demostrarExcepciones();
    }

    /**
     * Carga 4 empleados, uno de cada tipo, para poder ver el sistema en accion.
     */
    private static void registrarEmpleadosDemostracion(NominaServicio servicio) {
        try {
            EmpleadoAsalariado asalariado = new EmpleadoAsalariado(
                    "E001", "Ana Garcia",
                    LocalDate.of(2021, 3, 15),
                    3_500_000.0);
            servicio.registrarEmpleado(asalariado);

            EmpleadoPorHoras porHoras = new EmpleadoPorHoras(
                    "E002", "Carlos Mendoza",
                    LocalDate.of(2022, 7, 1),
                    25_000.0, 175.0);
            servicio.registrarEmpleado(porHoras);

            EmpleadoComision comision = new EmpleadoComision(
                    "E003", "Laura Rios",
                    LocalDate.of(2020, 1, 10),
                    1_500_000.0, 0.05, 15_000_000.0);
            servicio.registrarEmpleado(comision);

            EmpleadoTemporal temporal = new EmpleadoTemporal(
                    "E004", "Pedro Castillo",
                    LocalDate.of(2025, 12, 1),
                    85_000.0, 22,
                    LocalDate.of(2026, 6, 30));
            servicio.registrarEmpleado(temporal);

            System.out.println("O " + servicio.contarEmpleados()
                    + " empleados registrados correctamente.");
            System.out.println();

        } catch (HorasInvalidasException | VentasInvalidasException e) {
            System.err.println("[ERROR] Al crear empleado: " + e.getMessage());
        }
    }

    /**
     * Lanza y captura cada excepcion personalizada
     * para demostrar que el manejo de errores funciona.
     */
    private static void demostrarExcepciones() {
        System.out.println("=========================================");
        System.out.println("* DEMO DE EXCEPCIONES PERSONALIZADAS    *");
        System.out.println("=========================================");

        try {
            throw new SalarioNegativoException("Prueba de salario negativo", -500.0);
        } catch (SalarioNegativoException e) {
            System.out.println("  SalarioNegativoException capturada: " + e.getMessage());
        }

        try {
            throw new HorasInvalidasException("Horas fuera de rango", 250.0, 192.0);
        } catch (HorasInvalidasException e) {
            System.out.println("  HorasInvalidasException capturada  : " + e.getMessage());
        }

        try {
            throw new VentasInvalidasException("Ventas negativas", -1000.0);
        } catch (VentasInvalidasException e) {
            System.out.println("  VentasInvalidasException capturada : " + e.getMessage());
        }

        try {
            new EmpleadoAsalariado("", "Test", LocalDate.now().minusDays(1), 1000.0);
        } catch (IllegalArgumentException e) {
            System.out.println("  IllegalArgumentException capturada : " + e.getMessage());
        }

        System.out.println();
        System.out.println("======================================");
        System.out.println("*        Ejecucion completada        *");
        System.out.println("======================================");
    }
}
