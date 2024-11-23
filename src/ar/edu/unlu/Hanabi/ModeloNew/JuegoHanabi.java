package ar.edu.unlu.Hanabi.ModeloNew;
import java.util.ArrayList;
import java.util.List;

public class JuegoHanabi {
    private final List<Jugador> jugadores;  // Lista de jugadores activos
    private Tablero tablero;           // Tablero que contiene los castillos, mazo y fichas
    private int indiceTurnoActual;    // Índice del jugador que tiene el turno actual
    private boolean juegoTerminado;   // Indica si el juego ha finalizado

    // Constructor
    public JuegoHanabi() {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero(jugadores);  // Creación del tablero
        this.indiceTurnoActual = 0;
        this.juegoTerminado = false;
    }

    // Métodos

    public Jugador registrarJugador(String nombreJugador) {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");
        }

        if (jugadores.size() >= 4) {
            throw new IllegalStateException("No se pueden registrar más de 4 jugadores.");
        }

        // Crear el jugador y agregarlo a la lista
        Jugador nuevoJugador = new Jugador(nombreJugador.trim());
        jugadores.add(nuevoJugador);

        // Devolver el objeto Jugador creado
        return nuevoJugador;
    }

    public boolean juegoListoParaIniciar() {
        return jugadores.size() >= 2;
    }

    public void iniciarJuego(List<Jugador> jugadores) {
        // Verificar si hay al menos 2 jugadores
        if (!juegoListoParaIniciar()) {
            throw new IllegalStateException("No se puede iniciar el juego sin al menos 2 jugadores.");
        }
        tablero = new Tablero(jugadores);  // El constructor de Tablero recibe los jugadores
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

    /**
     * Verifica si se han cumplido las condiciones para terminar el juego.
     * @return true si el juego debe terminar, false en caso contrario.
     */
    public EstadoFinJuego verificarFinDeJuego() {
        // Verificar si todos los castillos están completos
        if (tablero.todosLosCastillosCompletos()) {
            return EstadoFinJuego.VICTORIA;
        }

        // Verificar si el mazo está vacío o si las fichas de vida se agotaron
        if (tablero.getMazoActual() == 0 || tablero.obtenerFichasDeVida() == 0) {
            return EstadoFinJuego.DERROTA;
        }

        // Si el juego continúa
        return EstadoFinJuego.JUEGO_EN_CURSO;
    }

    /**
     * Reduce una ficha de pista disponible.
     */
    public void restarFichaPista() {
        tablero.reducirFichaPista();
    }

    /**
     * Reduce una ficha de vida disponible.
     */
    public void restarFichaVida() {
        tablero.reducirFichaVida();
    }

    /**
     * Recupera una ficha de pista y la agrega al pool de fichas de pista.
     */
    public void recuperarFichaPista() {
        tablero.recuperarFichaPista();
    }

    public Jugador getJugadorActual() {
        return jugadores.get(indiceTurnoActual);
    }

    public Jugador obtenerJugadorPorNombre(String nombre) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                return jugador;
            }
        }
        return null;  // Si no se encuentra un jugador con ese nombre
    }

    public void jugadorJuegaCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }
        tablero.jugarCarta(jugador, carta);
        cambiarTurno();    // Cambia al siguiente turno automáticamente
    }

    public void jugadorDaPista(Jugador jugadorObjetivo, Pista pista) {
        if (jugadorObjetivo == null || pista == null) {
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }

        // Llamar al método darPista del Tablero, pasando el jugador objetivo y la pista
        tablero.darPista(jugadorObjetivo, pista);
        tablero.reducirFichaPista();
    }

    public void jugadorDescartaCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalStateException("No puedes descartar una carta porque no hay fichas de pista disponibles.");
        }
       // Si hay fichas de pista usadas, proceder con el descarte
        tablero.descartarCarta(jugador, carta);  // El jugador descarta la carta
        tablero.recuperarFichaPista();   // El tablero recupera una ficha de pista
        tablero.tomarCarta(jugador);            // El jugador toma una nueva carta
        cambiarTurno();                  // Cambia al siguiente turno
    }


    public void jugadorTomaCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalStateException("No puedes tomar una carta porque no hay cartas en el mazo.");
        }
        tablero.tomarCarta(jugador);

    }


    public Jugador[] getJugadores() {
        return jugadores.toArray(new Jugador[0]);
    }
    public Tablero getTablero() {
        return tablero;
    }
}



