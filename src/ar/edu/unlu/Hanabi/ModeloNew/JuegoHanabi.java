package ar.edu.unlu.Hanabi.ModeloNew;
import java.util.ArrayList;
import java.util.List;

public class JuegoHanabi {
    private final List<Jugador> jugadores;
    private Tablero tablero;
    private int indiceTurnoActual;

    public JuegoHanabi() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero(jugadores);
        this.indiceTurnoActual = 0;

    }

    public Jugador registrarJugador(String nombreJugador) {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");
        }

        if (jugadores.size() >= 5) {
            throw new IllegalStateException("No se pueden registrar más de 5 jugadores.");
        }

        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                throw new IllegalArgumentException("El nombre '" + nombreJugador + "' ya está en uso.");
            }
        }

        Jugador nuevoJugador = new Jugador(nombreJugador);
        jugadores.add(nuevoJugador);

        return nuevoJugador;
    }


    public boolean juegoListoParaIniciar() {
        return jugadores.size() >= 2;
    }

    public void iniciarJuego(List<Jugador> jugadores) {

        if (!juegoListoParaIniciar()) {
            throw new IllegalStateException("No se puede iniciar el juego sin al menos 2 jugadores.");
        }
        tablero = new Tablero(jugadores);
        tablero.repartirCartas(jugadores);
        indiceTurnoActual = 0;
        jugadores.get(indiceTurnoActual).setJugadorTurno(true);
    }


    public void iniciarTurno() {
        if (jugadores.isEmpty()) {
            throw new IllegalStateException("No hay jugadores registrados.");
        }
        Jugador jugadorEnTurno = jugadores.get(indiceTurnoActual);
        jugadorEnTurno.setJugadorTurno(true);
        System.out.println("Es el turno de: " + jugadorEnTurno.getNombre());
    }

    public void cambiarTurno() {
        jugadores.get(indiceTurnoActual).setJugadorTurno(false);
        indiceTurnoActual = (indiceTurnoActual + 1) % jugadores.size();
        jugadores.get(indiceTurnoActual).setJugadorTurno(true);
        System.out.println("Ahora es el turno de: " + jugadores.get(indiceTurnoActual).getNombre());
    }

    public EstadoFinJuego verificarFinDeJuego() {
        if (tablero.todosLosCastillosCompletos()) {
            return EstadoFinJuego.VICTORIA;
        }
        if (tablero.getMazoActual() == 0 || tablero.obtenerFichasDeVida() == 0) {
            return EstadoFinJuego.DERROTA;
        }
        return EstadoFinJuego.JUEGO_EN_CURSO;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(indiceTurnoActual);
    }

    public void jugadorJuegaCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }
        tablero.jugarCarta(jugador, carta);
        tablero.cartaJugada(carta);
        tablero.tomarCarta(jugador);

    }

    public void jugadorDaPista(Jugador jugadorObjetivo, Pista pista) {
        if (jugadorObjetivo == null || pista == null) {
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }
        tablero.darPista(jugadorObjetivo, pista);
        tablero.reducirFichaPista();

    }

    public void jugadorDescartaCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalStateException("No puedes descartar una carta porque no hay fichas de pista disponibles.");
        }
        tablero.descartarCarta(jugador, carta);
        tablero.recuperarFichaPista();
        tablero.tomarCarta(jugador);

    }
    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public Tablero getTablero() {
        return tablero;
    }


    // Método para crear una pista
    public Pista crearPista(TipoPista tipoPista, Object valor) {

        if (tipoPista == null || valor == null) {
            throw new IllegalArgumentException("Tipo de pista y valor no pueden ser nulos.");
        }

        if (tipoPista == TipoPista.COLOR && !(valor instanceof ColorCarta)) {
            throw new IllegalArgumentException("El valor debe ser un ColorCarta para el tipo de pista COLOR.");
        } else if (tipoPista == TipoPista.NUMERO && !(valor instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer para el tipo de pista NUMERO.");
        }
        return new Pista(tipoPista, valor);
    }
}





