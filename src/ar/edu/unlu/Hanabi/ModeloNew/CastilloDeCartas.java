package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.LinkedList;
import java.util.List;

public class CastilloDeCartas {
    private final ColorCarta color;
    private final List<Carta> cartas;

    public CastilloDeCartas(ColorCarta color) {
        this.color = color;
        this.cartas = new LinkedList<>();  // Usamos LinkedList para optimizar el acceso al último elemento
    }

    public boolean apilarCarta(Carta carta) {
        // Verificar si la carta tiene el color correcto
        if (carta.getColor() != this.color) {
            return false;  // No apilar si el color no coincide
        }

        // Si el castillo está vacío, solo se puede apilar el 1
        if (cartas.isEmpty()) {
            if (carta.getNumero() == 1) {
                cartas.add(carta);
                return true;
            }
        } else {
            // Si el castillo no está vacío, verificamos el número y el color
            Carta cartaSuperior = cartas.getLast();  // Usamos get() con índice en LinkedList
            if (carta.getNumero() == cartaSuperior.getNumero() + 1) {
                cartas.add(carta);
                return true;
            }
        }

        return false;  // Si no se puede apilar, devolvemos false
    }

    public boolean esCastilloCompleto() {
        if (cartas.size() != 5) {
            return false;
        }
        for (int i = 1; i <= 5; i++) {
            if (cartas.get(i - 1).getNumero() != i) {
                return false;
            }
        }
        return true;
    }

    public List<Carta> getCartas() {
        return List.copyOf(cartas);  // Devuelve una copia inmutable de la lista
    }

    public ColorCarta getColor() {
        return color;
    }
}


