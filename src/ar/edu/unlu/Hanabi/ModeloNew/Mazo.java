package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartas;

    // Constructor: Genera el mazo inicial
    public Mazo() {
        this.cartas = new ArrayList<>();
        generarCartas(); // Genera las cartas del mazo
        barajar();       // Baraja las cartas al inicio
    }

    // Genera las cartas según la distribución estándar de Hanabi
    private void generarCartas() {
        for (ColorCarta color : ColorCarta.values()) {
            // Añadir 3 cartas con número 1
            for (int i = 0; i < 3; i++) {
                cartas.add(new Carta(color, 1));
            }
            // Añadir 2 cartas con números 2, 3 y 4
            for (int i = 2; i <= 4; i++) {
                cartas.add(new Carta(color, i));
                cartas.add(new Carta(color, i));
            }
            // Añadir 1 carta con número 5
            cartas.add(new Carta(color, 5));
        }
    }

    // Baraja las cartas del mazo
    public void barajar() {
        Collections.shuffle(cartas);
    }

    // Roba una carta del mazo (si está vacío, devuelve null)
    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            return null; // No hay más cartas en el mazo
        }
        return cartas.removeLast();
    }

    // Devuelve el número de cartas restantes en el mazo
    public int cartasRestantes() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty(); // Retorna true si el mazo está vacío, false si no lo está
    }
}
