package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JuegoMostrable {
    private final JuegoHanabi juegoHanabi;


    public JuegoMostrable(JuegoHanabi juegoHanabi) {
        this.juegoHanabi = juegoHanabi;

    }

    public List<Carta> obtenerManoJugador1(Jugador jugador) {
        return jugador.getMano();
    }

    public List<Carta> obtenerManoJugadorVisible(Jugador jugador) {

        List<Carta> manoVisible = new ArrayList<>(jugador.getMano());

        for (Carta carta : jugador.getMano()) {

            Carta cartaVisible = new Carta(carta.getColor(), carta.getNumero());
            cartaVisible.revelar();
            manoVisible.add(cartaVisible);
        }

        return manoVisible;
    }




    public static List<Map<Jugador, List<Carta>>> obtenerManosRestantesJugadores(Jugador jugadorInstanciado, List<Jugador> listaJugadores) {
        List<Map<Jugador, List<Carta>>> manosVisiblesRestantes = new ArrayList<>();

        // Iteramos sobre la lista de jugadores proporcionada
        for (Jugador jugador : listaJugadores) {

            // Si es el jugador instanciado, lo omitimos
            if (jugador.equals(jugadorInstanciado)) {
                continue;
            }

            List<Carta> manoRevelada = new ArrayList<>(jugador.getMano());

            // Iteramos sobre las cartas de la mano del jugador
            for (Carta carta : jugador.getMano()) {
                // Crear una copia de la carta con el estado "revelado"
                Carta cartaRevelada = new Carta(carta.getColor(), carta.getNumero());
                cartaRevelada.revelar(); // Forzar visibilidad de la carta
                manoRevelada.add(cartaRevelada);
            }

            // Creamos un mapa que asocia el jugador con su mano revelada
            Map<Jugador, List<Carta>> jugadorYMano = new HashMap<>();
            jugadorYMano.put(jugador, manoRevelada);

            // Añadir el mapa a la lista de manos
            manosVisiblesRestantes.add(jugadorYMano);
        }

        return manosVisiblesRestantes;
    }





    public int obtenerCartasRestantesEnMazo() {
        return juegoHanabi.getTablero().getMazoActual();
    }

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
        return juegoHanabi.getTablero().getCastillos();
    }
    public List<Jugador> listaJugadores() {return juegoHanabi.getJugadores();}


}
