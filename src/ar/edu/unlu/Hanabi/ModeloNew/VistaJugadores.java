package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.List;
import java.util.Map;

public class VistaJugadores {
    private List<String> cartasJugadorActual; // Representación de las cartas del jugador actual
    private Map<String, List<String>> cartasOtrosJugadores; // Nombre del jugador -> Sus cartas visibles

    // Constructor
    public VistaJugadores(List<String> cartasJugadorActual, Map<String, List<String>> cartasOtrosJugadores) {
        this.cartasJugadorActual = cartasJugadorActual;
        this.cartasOtrosJugadores = cartasOtrosJugadores;
    }

    // Getters
    public List<String> getCartasJugadorActual() {

        return cartasJugadorActual;
    }

    public Map<String, List<String>> getCartasOtrosJugadores() {
        return cartasOtrosJugadores;
    }
}

