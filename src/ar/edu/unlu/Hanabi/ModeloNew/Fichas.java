package ar.edu.unlu.Hanabi.ModeloNew;

/**
 * Clase Fichas
 * Propósito: Encapsula la lógica de las fichas de pista y vida en el juego Hanabi.
 */
public class Fichas {
    private int fichasPistaDisponibles;  // Número de fichas de pista disponibles.
    private int fichasVida;   // Número de fichas de vida disponibles.
    private int fichasPistaUsadas;


    public Fichas() {
        this.fichasVida = 8;          // Inicialmente 8 fichas de vida
        this.fichasPistaDisponibles = 3;         // Inicialmente 3 fichas de pista
        this.fichasPistaUsadas = 0;  // Inicialmente 0 fichas de pista usadas
    }

    /**
     * Devuelve el número de fichas de pista disponibles.
     *
     * @return int - Número de fichas de pista disponibles.
     */
    public int getFichasDePista() {
        return fichasPistaDisponibles;
    }

    /**
     * Devuelve el número de fichas de vida disponibles.
     *
     * @return int - Número de fichas de vida disponibles.
     */
    public int getFichasDeVida() {
        return fichasVida;
    }

    /**
     * Reduce en una unidad el número de fichas de pista.
     */
    public void usarFichaPista() {
        if (fichasPistaDisponibles <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }
        fichasPistaDisponibles--;
        fichasPistaUsadas++;
    }

    /**
     * Reduce en una unidad el número de fichas de vida.
     */
    public void usarFichaVida() {
        if (fichasVida <= 0) {
            throw new IllegalStateException("No hay fichas de vida disponibles.");
        }
        fichasVida--;
    }

    /**
     * Recupera una ficha de pista, incrementando en una unidad su cantidad.
     */
    public void recuperarFichaPista() {
        fichasPistaUsadas--;
        fichasPistaDisponibles++;
    }

    public int getFichasPistaUsadas() {
        return fichasPistaUsadas;
    }

    @Override
    public String toString() {
        return "Fichas de Vida: " + fichasVida + ", Fichas de Pista: " + fichasPistaDisponibles + ", Fichas de Pistas Usadas: " + fichasPistaUsadas;
    }
}
