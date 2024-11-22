package ar.edu.unlu.Hanabi.Controlador;

import ar.edu.unlu.Hanabi.ModeloNew.*;

import java.util.ArrayList;
import java.util.List;


public class ControladorJuegoHanabi implements Observado {
    private final JuegoHanabi juegoHanabi;
    private final List<Observador> observadores;
    private final JuegoMostrable juegoMostrable;

    public ControladorJuegoHanabi(JuegoHanabi juegoHanabi, JuegoMostrable juegoMostrable) {
        this.juegoHanabi = juegoHanabi;
        this.juegoMostrable = juegoMostrable;
        this.observadores = new ArrayList<>();
    }

    // Métodos que manejan eventos

    public void iniciarJuego(List<Jugador> jugadores) {
        juegoHanabi.iniciarJuego(jugadores);
        notificarObservador(Eventos.JUEGO_INICIADO);
    }

    public void iniciarTurno() {
        juegoHanabi.iniciarTurno();
        notificarObservador(Eventos.INICIO_TURNO);
    }

    public void cambiarTurno() {
        // Verifica si el juego ha terminado después de cada cambio de turno
        EstadoFinJuego estado = juegoHanabi.verificarFinDeJuego();

        if (estado == EstadoFinJuego.VICTORIA) {
            notificarObservador(Eventos.VICTORIA);  // El juego ha terminado con victoria
            return;
        } else if (estado == EstadoFinJuego.DERROTA) {
            notificarObservador(Eventos.DERROTA);  // El juego ha terminado con derrota
            return;
        }

        // Si el juego sigue en curso, entonces cambia el turno
        juegoHanabi.cambiarTurno();
        notificarObservador(Eventos.CAMBIO_TURNO);
    }

    public void usarFichaPista() {
        if (juegoMostrable.obtenerFichasDePista() > 0) {
            juegoHanabi.restarFichaPista();
            notificarObservador(Eventos.FICHA_PISTA_USADA);
        } else {
            notificarObservador(Eventos.FALTA_FICHA_PISTA);
        }
    }

    public void restarFichaVida() {
        if (juegoMostrable.obtenerFichasDeVida() > 0) {
            juegoHanabi.restarFichaVida();
            notificarObservador(Eventos.FICHA_VIDA_PERDIDA);
        } else {
            notificarObservador(Eventos.FALTA_FICHA_VIDA);
        }
    }

    public void recuperarFichaPista() {
        juegoHanabi.recuperarFichaPista();
        notificarObservador(Eventos.FICHA_PISTA_RECUPERADA);
    }

    public int obtenerFichasDePistaDisponibles() {
        return juegoMostrable.obtenerFichasDePista();
    }

    public int obtenerFichasDeVidaDisponibles() {
        return juegoMostrable.obtenerFichasDeVida();
    }

    public int obtenerFichasDePistaUsadas() {
        return juegoMostrable.obtenerFichasDePistaUsadas();
    }

    public void jugadorJuegaCarta(Jugador jugador, Carta carta) {
        if (jugador != juegoHanabi.getJugadorActual()) {
            notificarObservador(Eventos.NO_ES_TURNO);
        } else {
            juegoHanabi.jugadorJuegaCarta(jugador, carta);
            notificarObservador(Eventos.JUGADOR_JUGO_CARTA);
        }
    }

    public void jugadorDaPista(Jugador jugador, Jugador objetivo, Pista pista) {
        if (jugador != juegoHanabi.getJugadorActual()) {
            notificarObservador(Eventos.NO_ES_TURNO); // Si no es el turno del jugador
            return;
        }
        if (juegoMostrable.obtenerFichasDePista() <= 0) {
            notificarObservador(Eventos.NO_HAY_PISTA_DIPONIBLE); // Si no hay fichas de pista
            return;
        }
        juegoHanabi.jugadorDaPista(objetivo, pista);
        notificarObservador(Eventos.FICHA_PISTA_USADA);
    }

    public void jugadorDescartaCarta(Jugador jugador, Carta carta) {
        if (jugador != juegoHanabi.getJugadorActual()) {
            notificarObservador(Eventos.NO_ES_TURNO); // Si no es el turno del jugador
            return;
        }

        if (juegoMostrable.obtenerFichasDePistaUsadas() <= 0) {
            notificarObservador(Eventos.NO_HAY_PISTAS_USADAS); // Si no hay pistas usadas
            return;
        }

        juegoHanabi.jugadorDescartaCarta(jugador, carta);
        notificarObservador(Eventos.JUGADOR_DESCARTO_CARTA);
    }

    public void jugadorTomaCarta(Jugador jugador) {
        if (jugador != juegoHanabi.getJugadorActual()) {
            notificarObservador(Eventos.NO_ES_TURNO);
        }

        juegoHanabi.jugadorTomaCarta(jugador, null);
        notificarObservador(Eventos.JUGADOR_TOMO_CARTA);
    }

    // Método para crear una pista
    public Pista crearPista(TipoPista tipoPista, Object valor) {
        // Verificamos que el tipo y valor sean correctos
        if (tipoPista == null || valor == null) {
            throw new IllegalArgumentException("Tipo de pista y valor no pueden ser nulos.");
        }

        // Validación adicional según el tipo de pista
        if (tipoPista == TipoPista.COLOR && !(valor instanceof ColorCarta)) {
            throw new IllegalArgumentException("El valor debe ser un ColorCarta para el tipo de pista COLOR.");
        } else if (tipoPista == TipoPista.NUMERO && !(valor instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer para el tipo de pista NUMERO.");
        }

        // Crear la pista
        Pista pista = new Pista(tipoPista, valor);

        // Notificar a los observadores que se ha creado una nueva pista
        notificarObservador(Eventos.PISTA_CREADA, pista);

        // Devolver la pista creada
        return pista;
    }

    public void obtenerNombreJugador(Jugador jugador) {
        String nombre = jugador.getNombre();
        notificarObservador(Eventos.OBTENER_NOMBRE_JUGADOR, nombre);
    }

    public void obtenerCantidadJugadores() {
        int cantidad = juegoHanabi.getJugadores().length;
        notificarObservador(Eventos.OBTENER_CANTIDAD_JUGADORES, cantidad);
    }

    public void obtenerJugadores() {
        Jugador[] jugadores = juegoHanabi.getJugadores();
        notificarObservador(Eventos.OBTENER_JUGADORES, jugadores);
    }
    // Método para obtener un jugador por su nombre
    public Jugador obtenerJugadorPorNombre(String nombre) {
        for (Jugador jugador : juegoHanabi.getJugadores()) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                return jugador;
            }
        }
        return null; // Si no se encuentra el jugador
    }

    // Mostrar la mano del jugador en turno
    public void mostrarManoJugadorTurno(Jugador jugadorTurno) {
        List<CartaRepresentacion> mano = juegoMostrable.obtenerManoJugadorTurno(jugadorTurno);
        notificarObservador(Eventos.MOSTRAR_MANO_JUGADOR_TURNO, mano);
    }

    // Mostrar las manos visibles para un jugador en espera
    public void mostrarManosParaJugadorEnEspera(Jugador jugadorEnEspera) {
        List<List<CartaRepresentacion>> manosVisibles = juegoMostrable.obtenerManosParaJugadorEnEspera(jugadorEnEspera);
        notificarObservador(Eventos.MOSTRAR_MANOS_JUGADOR_ESPERA, manosVisibles);
    }

    public void mostrarManoJugador(Jugador jugador) {
        String mano = JuegoMostrable.mostrarManoJugador(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO, mano);
    }

    public void mostrarManoJugadorPorNumero(Jugador jugador) {
        String mano = JuegoMostrable.mostrarManoJugadorPorNumero(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO_NUMEROS, mano);
    }

    public void mostrarManoJugadorPorColor(Jugador jugador) {
        String mano = JuegoMostrable.mostrarManoJugadorPorColor(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO_COLORES, mano);
    }

    public void registrarJugador(String nombreJugador) {
        try {
            juegoHanabi.registrarJugador(nombreJugador);
            notificarObservador(Eventos.JUGADOR_REGISTRADO, nombreJugador);
        } catch (IllegalArgumentException | IllegalStateException e) {
            notificarObservador(Eventos.ERROR_REGISTRO_JUGADOR, e.getMessage());
        }
    }

    public Jugador crearJugador(String nombreJugador) {
        try {
            Jugador nuevoJugador = juegoHanabi.registrarJugador(nombreJugador);
            notificarObservador(Eventos.JUGADOR_CREADO, nuevoJugador);
            return nuevoJugador;
        } catch (IllegalArgumentException e) {
            notificarObservador(Eventos.ERROR_CREACION_JUGADOR, e.getMessage());
        }
        return null;
    }
    public Jugador obtenerJugadorActual() {
        return juegoHanabi.obtenerJugadorTurno();
    }

    @Override
    public void agregarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    @Override
    public void notificarObservador(Eventos evento) {
        for (Observador observador : observadores) {
            observador.notificar(evento);
        }
    }

    @Override
    public void notificarObservador(Eventos evento, Object data) {
        for (Observador observador : observadores) {
            observador.notificar(evento, data);
        }
    }
}