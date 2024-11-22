package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JuegoMostrable {
    private JuegoHanabi juegoHanabi; // Puede contener la lógica completa del juego
    private Tablero tablero; // Solo se usa para representar el estado del tablero



    public JuegoMostrable(JuegoHanabi juegoHanabi) {
        this.juegoHanabi = juegoHanabi;
        this.tablero = juegoHanabi.getTablero(); // Acceso al tablero
    }

    // Mano del jugador de turno
    public List<CartaRepresentacion> obtenerManoJugadorTurno(Jugador jugador) {
        List<CartaRepresentacion> representacionMano = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            // Se crea una representación de la carta usando el color, valor y si está revelada
            representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), carta.esRevelada()));
        }
        return representacionMano;
    }

    // Manos visibles para un jugador en espera
    public List<List<CartaRepresentacion>> obtenerManosParaJugadorEnEspera(Jugador jugadorEnEspera) {
        List<List<CartaRepresentacion>> representacionManos = new ArrayList<>();

        // Obtener jugadores directamente del tablero
        List<Jugador> jugadores = tablero.obtenerJugadores();

        for (Jugador jugador : jugadores) {
            List<CartaRepresentacion> representacionMano = new ArrayList<>();

            // Si el jugador es el que está en espera, sus cartas estarán ocultas
            if (jugador.equals(jugadorEnEspera)) {
                for (Carta carta : jugador.getMano()) {
                    representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), false)); // Oculto
                }
            } else {
                // Si no, las cartas serán visibles
                for (Carta carta : jugador.getMano()) {
                    representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), true)); // Visible
                }
            }

            representacionManos.add(representacionMano);
        }

        return representacionManos;
    }




    public static List<CartaRepresentacion> mostrarManoJugador(Jugador jugador) {
        List<CartaRepresentacion> representacionMano = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), carta.esRevelada()));
        }
        return representacionMano; // Devuelve la lista de cartas representadas (visibles o no)
    }


    public static List<CartaRepresentacion> mostrarManoJugadorPorNumero(Jugador jugador) {
        List<CartaRepresentacion> representacionMano = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            boolean esVisible = carta.esRevelada();  // Solo se mostrará el número si la carta está revelada
            representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), esVisible));
        }
        return representacionMano;
    }


    public static List<CartaRepresentacion> mostrarManoJugadorPorColor(Jugador jugador) {
        List<CartaRepresentacion> representacionMano = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            boolean esVisible = carta.esRevelada();  // Solo se mostrará el color si la carta está revelada
            representacionMano.add(new CartaRepresentacion(carta.getColor(), carta.getNumero(), esVisible));
        }
        return representacionMano;
    }






    public int obtenerCartasRestantesEnMazo() {
        return tablero.getMazoActual();  // Obtener cartas restantes del mazo
    }


    // Representación de las fichas de vida y pista
    public int obtenerFichasDeVida() {
        return tablero.obtenerFichasDeVida();
    }

    public int obtenerFichasDePistaUsadas() {
        return tablero.obtenerFichasDePistaUsadas();
    }

    public int obtenerFichasDePista() {
        return tablero.obtenerFichasDePista();
    }

    // Representación de los castillos (cartas apiladas por color)
    public Map<ColorCarta, List<Carta>> obtenerEstadoCastillos() {
        Map<ColorCarta, List<Carta>> estadoCastillos = new HashMap<>();
        for (CastilloDeCartas castillo : tablero.getCastillos()) {
            estadoCastillos.put(castillo.getColor(), castillo.getCartas());
        }
        return estadoCastillos;
    }

}
