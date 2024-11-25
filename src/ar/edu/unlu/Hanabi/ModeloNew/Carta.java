package ar.edu.unlu.Hanabi.ModeloNew;


public class Carta {
    private final ColorCarta color;
    private final int numero;
    private boolean revelada; // Indica si la carta está revelada al jugador

    // Constructor
    public Carta(ColorCarta color, int numero) {
        if (numero < 1 || numero > 5) {
            throw new IllegalArgumentException("Número de carta inválido: " + numero);
        }
        this.color = color;
        this.numero = numero;
        this.revelada = false;
    }

    // Getters
    public ColorCarta getColor() {
        return color;
    }

    public int getNumero() {
        return numero;
    }

    // Métodos de revelación
    public boolean esRevelada() {
        return revelada;
    }

    public void revelar() {     this.revelada = true;    }



    // Método de coincidencia con pista
    public boolean coincideConPista(Pista pista) {
        if (pista == null || pista.getValor() == null) {
            throw new IllegalArgumentException("La pista no puede ser nula.");
        }

        // Revisamos el tipo de pista y comparamos el valor
        switch (pista.getTipoPista()) {
            case COLOR:
                return this.color.equals(pista.getValor());  // Aquí 'color' es un campo de la clase que almacena el color de la carta
            case NUMERO:
                return this.numero == (Integer) pista.getValor(); // Aquí 'numero' es un campo que almacena el número de la carta
            default:
                return false;
        }
    }



    @Override
    public String toString() {
        return color.name().toLowerCase() + " " + numero;
    }
}


