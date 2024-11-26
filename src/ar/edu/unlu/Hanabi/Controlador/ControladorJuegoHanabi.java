package ar.edu.unlu.Hanabi.Controlador;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;





public class ControladorJuegoHanabi implements Observado {

    private final JuegoHanabi juegoHanabi;
    private final List<Observador> observadores;
    private final JuegoMostrable juegoMostrable;


    public ControladorJuegoHanabi(JuegoHanabi juegoHanabi, JuegoMostrable juegoMostrable) {
        this.juegoHanabi = juegoHanabi;
        this.juegoMostrable = juegoMostrable;
        this.observadores = new ArrayList<>();

    }

    public void iniciarJuego(List<Jugador> jugadores) {
        juegoHanabi.iniciarJuego(jugadores);
        notificarObservador(Eventos.INICIAR_JUEGO);
    }

    public void iniciarTurno() {
        juegoHanabi.iniciarTurno();

    }

    public void cambiarTurno() {
        EstadoFinJuego estadoFinJuego = juegoHanabi.verificarFinDeJuego();

        if (estadoFinJuego == EstadoFinJuego.VICTORIA) {
            notificarObservador(Eventos.VICTORIA);
            return;
        } else if (estadoFinJuego == EstadoFinJuego.DERROTA) {
            notificarObservador(Eventos.DERROTA);
            return;
        }

        juegoHanabi.cambiarTurno();
        notificarObservador(Eventos.CAMBIO_TURNO);
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
            cambiarTurno();
        }
    }

    public void jugadorDaPista(Jugador objetivo, Pista pista) {
        if (juegoMostrable.obtenerFichasDePista() <= 0) {
            notificarObservador(Eventos.NO_HAY_PISTA_DIPONIBLE); // Si no hay fichas de pista
            return;
        }
        juegoHanabi.jugadorDaPista(objetivo, pista);
        notificarObservador(Eventos.PISTA_DADA);
        cambiarTurno();
    }

    public List<Jugador> obtenerListaJugadores() {return juegoHanabi.getJugadores();}

    public List<Carta> obtenerManoJugadorNoVisible(Jugador jugadorInstanciado) {
        return juegoMostrable.obtenerManoJugador1(jugadorInstanciado);
    }
    public List<Carta> obtenerManoJugadorVisible(Jugador jugadorInstanciado) {
        return juegoMostrable.obtenerManoJugadorVisible(jugadorInstanciado);
    }

    public List<Map<Jugador, List<Carta>>> retornarManosVisiblesJugadores(Jugador jugadorInstanciado, List<Jugador> lista) {

        return juegoMostrable.obtenerManosRestantesJugadores(jugadorInstanciado, lista);
    }
    public List<Object> obtenerDatosTablero() {
        List<Object> datosTablero = new ArrayList<>();
        datosTablero.add(juegoMostrable.obtenerCartasRestantesEnMazo());
        datosTablero.add(juegoMostrable.obtenerFichasDeVida());
        datosTablero.add(juegoMostrable.obtenerFichasDePistaUsadas());
        datosTablero.add(juegoMostrable.obtenerFichasDePista());
        datosTablero.add(juegoMostrable.obtenerCastillos());

        return datosTablero;
    }
    public Jugador obtenerJugadorActual() {
        return juegoHanabi.getJugadorActual();

    }

    //Observador

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
        cambiarTurno();
    }

    public Pista crearPista(TipoPista tipoPista, Object valor) {
        Pista pistaCreada = juegoHanabi.crearPista(tipoPista, valor);
        notificarObservador(Eventos.PISTA_CREADA);
        return pistaCreada;

    }

    public void obtenerNombreJugador(Jugador jugador) {
        String nombre = jugador.getNombre();
        notificarObservador(Eventos.OBTENER_NOMBRE_JUGADOR, nombre);
    }

    public void obtenerManoJugadorObervador(Jugador jugadorInstanciado) {
        List<Carta> manoJugador = juegoMostrable.obtenerManoJugador1(jugadorInstanciado);
        notificarObservador(Eventos.MOSTRAR_MANO, manoJugador);
    }
    public void manejarObtenerManosVisiblesRestoObservador(Jugador jugadorInstanciado, List<Jugador> lista) {
        Map<Jugador, List<Carta>> listaManosVisibles = (Map<Jugador, List<Carta>>) JuegoMostrable.obtenerManosRestantesJugadores(jugadorInstanciado, lista);
        notificarObservador(Eventos.MOSTRAR_MANOS_VISIBLES_RESTO, listaManosVisibles);
    }


    public void obtenerDatosTableroObserver() {
        List<Object> datosTablero = new ArrayList<>();
        datosTablero.add(juegoMostrable.obtenerCartasRestantesEnMazo());
        datosTablero.add(juegoMostrable.obtenerFichasDeVida());
        datosTablero.add(juegoMostrable.obtenerFichasDePistaUsadas());
        datosTablero.add(juegoMostrable.obtenerFichasDePista());
        datosTablero.add(juegoMostrable.obtenerCastillos());
        notificarObservador(Eventos.ACTUALIZAR_TABLERO, datosTablero);

    }



    public void registrarJugador(String nombreJugador) {
        try {
            Jugador juevoJugador = juegoHanabi.registrarJugador(nombreJugador);
            notificarObservador(Eventos.JUGADOR_CREADO, juevoJugador);
        } catch (IllegalArgumentException e) {
            notificarObservador(Eventos.ERROR_CREACION_JUGADOR, e.getMessage());
        }
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