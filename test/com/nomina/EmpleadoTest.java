package com.nomina;

import com.nomina.excepcion.HorasInvalidasException;
import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.excepcion.VentasInvalidasException;
import com.nomina.modelo.EmpleadoAsalariado;
import com.nomina.modelo.EmpleadoComision;
import com.nomina.modelo.EmpleadoPorHoras;
import com.nomina.modelo.EmpleadoTemporal;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Pruebas unitarias para las clases del modelo de empleados.
 *
 * <p>Valida los calculos salariales brutos, netos, deducciones y beneficios
 * para los diferentes tipos de empleados, asi como el comportamiento de las
 * excepciones personalizadas y las validaciones de atributos.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 */
public class EmpleadoTest {

    // ── Pruebas de EmpleadoAsalariado ──────────────────────────────────────

    @Test
    public void testEmpleadoAsalariadoCalculos() throws SalarioNegativoException {
        double salarioBase = 3_000_000.0;
        EmpleadoAsalariado emp = new EmpleadoAsalariado("E101", "Ana Gomez", LocalDate.now(), salarioBase);

        // Bruto = salario base
        Assert.assertEquals(salarioBase, emp.calcularSalarioBruto(), 0.001);

        // Beneficios = prima (8.33% de base) -> 3_000_000 * 0.0833 = 249_900
        double beneficiosEsperados = salarioBase * 0.0833;
        Assert.assertEquals(beneficiosEsperados, emp.calcularBeneficios(), 0.001);

        // Deducciones = salud (4%) + pension (4%) -> 3_000_000 * 0.08 = 240_000
        double deduccionesEsperadas = salarioBase * 0.08;
        Assert.assertEquals(deduccionesEsperadas, emp.calcularDeducciones(), 0.001);

        // Neto = bruto + beneficios - deducciones
        double netoEsperado = salarioBase + beneficiosEsperados - deduccionesEsperadas;
        Assert.assertEquals(netoEsperado, emp.calcularSalarioNeto(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpleadoAsalariadoSalarioNegativo() {
        new EmpleadoAsalariado("E101", "Ana Gomez", LocalDate.now(), -100.0);
    }

    // ── Pruebas de EmpleadoPorHoras ────────────────────────────────────────

    @Test
    public void testEmpleadoPorHorasCalculosOrdinarios() throws HorasInvalidasException, SalarioNegativoException {
        // 150 horas (menor a la jornada ordinaria de 160)
        double tarifa = 20_000.0;
        double horas = 150.0;
        EmpleadoPorHoras emp = new EmpleadoPorHoras("E102", "Juan Castro", LocalDate.now(), tarifa, horas);

        double brutoEsperado = horas * tarifa; // 3_000_000
        Assert.assertEquals(brutoEsperado, emp.calcularSalarioBruto(), 0.001);

        // Beneficio: Auxilio de transporte si bruto < 2_600_000
        // En este caso bruto = 3_000_000, entonces beneficios = 0
        Assert.assertEquals(0.0, emp.calcularBeneficios(), 0.001);

        // Deducciones: 8% del bruto -> 3_000_000 * 0.08 = 240_000
        Assert.assertEquals(brutoEsperado * 0.08, emp.calcularDeducciones(), 0.001);
    }

    @Test
    public void testEmpleadoPorHorasCalculosExtras() throws HorasInvalidasException, SalarioNegativoException {
        // 170 horas (10 horas extra)
        double tarifa = 10_000.0;
        double horas = 170.0;
        EmpleadoPorHoras emp = new EmpleadoPorHoras("E102", "Juan Castro", LocalDate.now(), tarifa, horas);

        // Bruto = 160 * 10_000 + 10 * 10_000 * 1.5 = 1_600_000 + 150_000 = 1_750_000
        double brutoEsperado = (160 * tarifa) + (10 * tarifa * 1.5);
        Assert.assertEquals(brutoEsperado, emp.calcularSalarioBruto(), 0.001);

        // Beneficio: Auxilio transporte (117,172) por ser bruto < 2_600_000
        Assert.assertEquals(117172.0, emp.calcularBeneficios(), 0.001);
    }

    @Test(expected = HorasInvalidasException.class)
    public void testEmpleadoPorHorasExcedidas() throws HorasInvalidasException {
        new EmpleadoPorHoras("E102", "Juan", LocalDate.now(), 10_000.0, 200.0);
    }

    // ── Pruebas de EmpleadoComision ────────────────────────────────────────

    @Test
    public void testEmpleadoComisionCalculos() throws VentasInvalidasException, SalarioNegativoException {
        double base = 1_200_000.0;
        double pct = 0.10;
        double ventas = 12_000_000.0; // Supera meta de 10_000_000 para bono
        EmpleadoComision emp = new EmpleadoComision("E103", "Maria Ruiz", LocalDate.now(), base, pct, ventas);

        // Bruto = base + ventas * pct = 1_200_000 + 1_200_000 = 2_400_000
        double brutoEsperado = base + (ventas * pct);
        Assert.assertEquals(brutoEsperado, emp.calcularSalarioBruto(), 0.001);

        // Beneficios: Bono por meta de ventas (200_000)
        Assert.assertEquals(200_000.0, emp.calcularBeneficios(), 0.001);
    }

    @Test(expected = VentasInvalidasException.class)
    public void testEmpleadoComisionVentasNegativas() throws VentasInvalidasException {
        new EmpleadoComision("E103", "Maria", LocalDate.now(), 1_000_000.0, 0.05, -500.0);
    }

    // ── Pruebas de EmpleadoTemporal ────────────────────────────────────────

    @Test
    public void testEmpleadoTemporalCalculos() throws SalarioNegativoException {
        double diario = 80_000.0;
        int dias = 20;
        EmpleadoTemporal emp = new EmpleadoTemporal(
                "E104", "Luis", LocalDate.now().minusDays(5),
                diario, dias, LocalDate.now().plusDays(5));

        // Bruto = diario * dias = 1_600_000
        double brutoEsperado = diario * dias;
        Assert.assertEquals(brutoEsperado, emp.calcularSalarioBruto(), 0.001);

        // Beneficios = 0
        Assert.assertEquals(0.0, emp.calcularBeneficios(), 0.001);

        // Deducciones = Retencion (1%) + ARL (0.522%) = 1.522% -> 1_600_000 * 0.01522 = 24_352
        double deduccionesEsperadas = brutoEsperado * (0.01 + 0.00522);
        Assert.assertEquals(deduccionesEsperadas, emp.calcularDeducciones(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpleadoTemporalFechaFinInvalida() {
        new EmpleadoTemporal(
                "E104", "Luis", LocalDate.now(),
                50_000.0, 10, LocalDate.now().minusDays(1));
    }

    // ── Pruebas de Identidad y Validaciones Comunes ─────────────────────────

    @Test(expected = IllegalArgumentException.class)
    public void testEmpleadoIdNulo() {
        new EmpleadoAsalariado(null, "Ana", LocalDate.now(), 1_000_000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpleadoIdVacio() {
        new EmpleadoAsalariado("   ", "Ana", LocalDate.now(), 1_000_000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpleadoFechaFutura() {
        new EmpleadoAsalariado("E105", "Ana", LocalDate.now().plusDays(1), 1_000_000.0);
    }
}

