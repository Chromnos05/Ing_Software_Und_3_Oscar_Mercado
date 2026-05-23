package com.nomina.excepcion;

/**
 * Salio un sueldo negativo. Eso no deberia pasar nunca,
 * pero si ocurre, esta excepcion dice claramente donde y por que.
 */
public class SalarioNegativoException extends Exception {

    private static final long serialVersionUID = 1L;

    private final double salarioCalculado;

    public SalarioNegativoException(String mensaje) {
        super(mensaje);
        this.salarioCalculado = 0.0;
    }

    public SalarioNegativoException(String mensaje, double salarioCalculado) {
        super(mensaje + " [Valor calculado: " + salarioCalculado + "]");
        this.salarioCalculado = salarioCalculado;
    }

    public SalarioNegativoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.salarioCalculado = 0.0;
    }

    public double getSalarioCalculado() {
        return salarioCalculado;
    }
}
