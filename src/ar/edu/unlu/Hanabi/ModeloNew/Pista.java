package ar.edu.unlu.Hanabi.ModeloNew;

public class Pista {
    private final TipoPista tipo;           // El tipo de la pista (COLOR o NUMERO)
    private final Object valor;             // El valor de la pista (puede ser un color o un número)

    /**
     * Constructor que crea una nueva pista con el tipo y valor.
     *
     * @param tipo Tipo de pista (COLOR o NUMERO)
     * @param valor Valor de la pista (puede ser un color o número)
     */
    public Pista(TipoPista tipo, Object valor) {
        if (tipo == null || valor == null) {
            throw new IllegalArgumentException("Los parámetros tipo y valor no pueden ser nulos.");
        }

        // Validación adicional según el tipo de pista
        if (tipo == TipoPista.COLOR && !(valor instanceof ColorCarta)) {
            throw new IllegalArgumentException("El valor debe ser un ColorCarta para el tipo de pista COLOR.");
        } else if (tipo == TipoPista.NUMERO && !(valor instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer para el tipo de pista NUMERO.");
        }

        this.tipo = tipo;
        this.valor = valor;
    }

    /**
     * Devuelve el tipo de pista (COLOR o NUMERO).
     *
     * @return TipoPista - El tipo de pista.
     */
    public TipoPista getTipoPista() {
        return tipo;
    }

    /**
     * Devuelve el valor de la pista.
     *
     * @return Object - El valor de la pista (color o número).
     */
    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Pista{" +
                "tipo=" + tipo +
                ", valor=" + valor +
                '}';
    }
}