package com.nomina;

import com.nomina.excepcion.HorasInvalidasException;
import com.nomina.excepcion.SalarioNegativoException;
import com.nomina.excepcion.VentasInvalidasException;
import com.nomina.modelo.Empleado;
import com.nomina.modelo.EmpleadoAsalariado;
import com.nomina.modelo.EmpleadoPorHoras;
import com.nomina.modelo.TipoEmpleado;
import com.nomina.repositorio.RepositorioEmpleadoMemoria;
import com.nomina.servicio.NominaServicio;
import com.nomina.servicio.ResumenNomina;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para la capa de servicio y persistencia en memoria.
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public class NominaServicioTest {

    private RepositorioEmpleadoMemoria repositorio;
    private NominaServicio servicio;

    @Before
    public void setUp() {
        repositorio = new RepositorioEmpleadoMemoria();
        servicio = new NominaServicio(repositorio);
    }

    @Test
    public void testRegistrarYBuscarEmpleado() {
        Empleado emp = new EmpleadoAsalariado("E501", "Pedro", LocalDate.now(), 2_000_000.0);
        servicio.registrarEmpleado(emp);

        Assert.assertEquals(1, servicio.contarEmpleados());

        Optional<Empleado> encontrado = servicio.buscarEmpleado("E501");
        Assert.assertTrue(encontrado.isPresent());
        Assert.assertEquals("Pedro", encontrado.get().getNombre());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarDuplicado() {
        Empleado emp1 = new EmpleadoAsalariado("E501", "Pedro", LocalDate.now(), 2_000_000.0);
        Empleado emp2 = new EmpleadoAsalariado("E501", "Pedro Duplicado", LocalDate.now(), 2_500_000.0);

        servicio.registrarEmpleado(emp1);
        servicio.registrarEmpleado(emp2); // Lanza excepcion
    }

    @Test
    public void testEliminarEmpleado() {
        Empleado emp = new EmpleadoAsalariado("E502", "Juan", LocalDate.now(), 2_000_000.0);
        servicio.registrarEmpleado(emp);
        Assert.assertEquals(1, servicio.contarEmpleados());

        boolean eliminado = servicio.eliminarEmpleado("E502");
        Assert.assertTrue(eliminado);
        Assert.assertEquals(0, servicio.contarEmpleados());

        boolean noEliminado = servicio.eliminarEmpleado("E502");
        Assert.assertFalse(noEliminado);
    }

    @Test
    public void testAssertFalseCorrectSyntax() {
        boolean noEliminado = servicio.eliminarEmpleado("INEXISTENTE");
        Assert.assertFalse(noEliminado);
    }

    @Test
    public void testFiltrarPorTipo() throws HorasInvalidasException {
        Empleado emp1 = new EmpleadoAsalariado("E503", "Juan", LocalDate.now(), 2_000_000.0);
        Empleado emp2 = new EmpleadoPorHoras("E504", "Maria", LocalDate.now(), 15_000.0, 100.0);
        Empleado emp3 = new EmpleadoAsalariado("E505", "Luis", LocalDate.now(), 3_000_000.0);

        servicio.registrarEmpleado(emp1);
        servicio.registrarEmpleado(emp2);
        servicio.registrarEmpleado(emp3);

        List<Empleado> asalariados = servicio.filtrarPorTipo(TipoEmpleado.ASALARIADO);
        Assert.assertEquals(2, asalariados.size());

        List<Empleado> porHoras = servicio.filtrarPorTipo(TipoEmpleado.POR_HORAS);
        Assert.assertEquals(1, porHoras.size());
    }

    @Test
    public void testCalcularTotalNomina() throws HorasInvalidasException, SalarioNegativoException {
        // Asalariado: Base = 3,000,000. prima = 249,900. deducciones = 240,000. Neto = 3,009,900.0
        Empleado emp1 = new EmpleadoAsalariado("E506", "Juan", LocalDate.now(), 3_000_000.0);

        // Por horas: 100 * 20,000 = 2,000,000. Auxilio = 117,172. Deducciones = 160,000. Neto = 1,957,172.0
        Empleado emp2 = new EmpleadoPorHoras("E507", "Maria", LocalDate.now(), 20_000.0, 100.0);

        servicio.registrarEmpleado(emp1);
        servicio.registrarEmpleado(emp2);

        double netoEsperado = emp1.calcularSalarioNeto() + emp2.calcularSalarioNeto();
        double totalNominaCalculado = servicio.calcularTotalNomina();

        Assert.assertEquals(netoEsperado, totalNominaCalculado, 0.001);
    }
}
