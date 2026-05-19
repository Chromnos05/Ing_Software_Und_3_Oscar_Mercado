package com.nomina.excepcion;

/**
 * Excepcion lanzada cuando el calculo de un salario produce un valor negativo.
 *
 * <p>Un salario negativo es un estado invalido en el sistema de nomina;
 * esta excepcion permite identificar la causa especifica del error.</p>
 *
 * @author Sistema Nomina
 * @version 1.0
 */
public class SalarioNegativoException extends Exception {

    /** Identificador de version de serializacion. */
    private static final long serialVersionUID = 1L;

    /** Valor de salario que causo la excepcion. */
    private final double salarioCalculado;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripcion del error
     */
    public SalarioNegativoException(String mensaje) {
        super(mensaje);
        this.salarioCalculado = 0.0;
    }

    /**
     * Constructor con mensaje y valor de salario que origino el error.
     *
     * @param mensaje          descripcion del error
     * @param salarioCalculado valor negativo que fue calculado
     */
    public SalarioNegativoException(String mensaje, double salarioCalculado) {
        super(mensaje + " [Valor calculado: " + salarioCalculado + "]");
        this.salarioCalculado = salarioCalculado;
    }

    /**
     * Constructor con causa raiz.
     *
     * @param mensaje descripcion del error
     * @param causa   excepcion que origino este error
     */
    public SalarioNegativoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.salarioCalculado = 0.0;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna el valor de salario negativo que desencadeno la excepcion.
     *
     * @return salario calculado (valor negativo)
     */
    public double getSalarioCalculado() {
        return salarioCalculado;
    }
}

