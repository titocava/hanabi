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

    public List<String> obtenerManoJugador(Jugador jugador) {
        List<String> cartasMano = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            if (carta.esRevelada()) {
                cartasMano.add("visible: " + carta.toString()); // Mostrar carta como visible con su color y número
            } else {
                cartasMano.add("no visible"); // Indicar que la carta está oculta
            }
        }
        return cartasMano;
    }


    public List<Map<String, List<String>>> obtenerManosVisiblesResto(Jugador jugadorInstanciado) {
        List<Map<String, List<String>>> listaManosVisibles = new ArrayList<>();

        for (Jugador jugador : juegoHanabi.getJugadores()) { // Usar el método getJugadores()
            if (!jugador.equals(jugadorInstanciado)) { // Excluir al jugador instanciado
                Map<String, List<String>> jugadorConMano = new HashMap<>();
                List<String> cartasVisibles = new ArrayList<>();

                for (Carta carta : jugador.getMano()) {
                    // Mostrar siempre el color y número de las cartas
                    cartasVisibles.add(carta.getColor() + " " + carta.getNumero());
                }

                jugadorConMano.put(jugador.getNombre(), cartasVisibles);
                listaManosVisibles.add(jugadorConMano);
            }
        }
        return listaManosVisibles;
    }



    public VistaJugadores mostrarVistaJugadores1(Jugador jugadorActual, List<Jugador> listaJugadores) {
        // Implementación correcta que devuelve un objeto VistaJugadores
        List<String> cartasJugadorActual = new ArrayList<>();
        Map<String, List<String>> cartasOtrosJugadores = new HashMap<>();

        // Procesar las cartas del jugador actual
        for (Carta carta : jugadorActual.getMano()) {
            if (carta.esRevelada()) {
                cartasJugadorActual.add(carta.toString());
            } else {
                cartasJugadorActual.add("??");
            }
        }

        // Procesar las cartas de los demás jugadores
        for (Jugador jugador : listaJugadores) {
            if (!jugador.equals(jugadorActual)) {
                List<String> cartasVisibles = new ArrayList<>();
                for (Carta carta : jugador.getMano()) {
                    cartasVisibles.add(carta.toString());
                }
                cartasOtrosJugadores.put(jugador.getNombre(), cartasVisibles);
            }
        }

        return new VistaJugadores(cartasJugadorActual, cartasOtrosJugadores);
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
        return juegoHanabi.getTablero().getMazoActual();  // Obtener cartas restantes del mazo
    }


    // Representación de las fichas de vida y pista
    public int obtenerFichasDeVida() {
        return juegoHanabi.getTablero().obtenerFichasDeVida();
    }

    public int obtenerFichasDePistaUsadas() {
        return juegoHanabi.getTablero().obtenerFichasDePistaUsadas();
    }

    public int obtenerFichasDePista() {
        return juegoHanabi.getTablero().obtenerFichasDePista();
    }


    public List<CastilloDeCartas> obtenerCastillos() {
        return juegoHanabi.getTablero().getCastillos();  // Accede al tablero y obtiene la lista de castillos
    }



}
