package com.nomina.excepcion;

/**
 * Excepción lanzada cuando el cálculo de un salario produce un valor negativo.
 *
 * <p>Un salario negativo es un estado inválido en el sistema de nómina;
 * esta excepción permite identificar la causa específica del error.</p>
 *
 * @author Sistema Nómina
 * @version 1.0
 */
public class SalarioNegativoException extends Exception {

    /** Identificador de versión de serialización. */
    private static final long serialVersionUID = 1L;

    /** Valor de salario que causó la excepción. */
    private final double salarioCalculado;

    // ── Constructores ──────────────────────────────────────────────────────

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public SalarioNegativoException(String mensaje) {
        super(mensaje);
        this.salarioCalculado = 0.0;
    }

    /**
     * Constructor con mensaje y valor de salario que originó el error.
     *
     * @param mensaje          descripción del error
     * @param salarioCalculado valor negativo que fue calculado
     */
    public SalarioNegativoException(String mensaje, double salarioCalculado) {
        super(mensaje + " [Valor calculado: " + salarioCalculado + "]");
        this.salarioCalculado = salarioCalculado;
    }

    /**
     * Constructor con causa raíz.
     *
     * @param mensaje descripción del error
     * @param causa   excepción que originó este error
     */
    public SalarioNegativoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.salarioCalculado = 0.0;
    }

    // ── Getter ─────────────────────────────────────────────────────────────

    /**
     * Retorna el valor de salario negativo que desencadenó la excepción.
     *
     * @return salario calculado (valor negativo)
     */
    public double getSalarioCalculado() {
        return salarioCalculado;
    }
}
