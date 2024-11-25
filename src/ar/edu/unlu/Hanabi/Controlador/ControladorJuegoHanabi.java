package ar.edu.unlu.Hanabi.Controlador;

import ar.edu.unlu.Hanabi.ModeloNew.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ControladorJuegoHanabi implements Observado {
    private final JuegoHanabi juegoHanabi;
    private final List<Observador> observadores;
    private final JuegoMostrable juegoMostrable;
    private Jugador nuevojugador;

    public ControladorJuegoHanabi(JuegoHanabi juegoHanabi, JuegoMostrable juegoMostrable) {
        this.juegoHanabi = juegoHanabi;
        this.juegoMostrable = juegoMostrable;
        this.observadores = new ArrayList<>();

    }

    // Métodos que manejan eventos

    public void iniciarJuego(List<Jugador> jugadores) {
        juegoHanabi.iniciarJuego(jugadores);
        notificarObservador(Eventos.INICIAR_JUEGO);
    }

    public void iniciarTurno() {
        juegoHanabi.iniciarTurno();
       // notificarObservador(Eventos.INICIO_TURNO);
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
        if (juegoMostrable.obtenerFichasDePistaUsadas() > 0) {
            juegoHanabi.recuperarFichaPista();
            notificarObservador(Eventos.FICHA_PISTA_RECUPERADA);
        } else {
            notificarObservador(Eventos.FALTA_FICHA_PISTA_USADA);
        }
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

    /*public void obtenerCantidadJugadores() {
        int cantidad = juegoHanabi.getJugadores().length;
        notificarObservador(Eventos.OBTENER_CANTIDAD_JUGADORES, cantidad);
    }*/



    public void obtenerJugadores() {
        // Obtener la lista de jugadores desde JuegoHanabi
        List<Jugador> jugadores = juegoHanabi.getJugadores();
        notificarObservador(Eventos.OBTENER_JUGADORES, jugadores);
    }

    public List<Jugador> obtenerListaJugadores() {return juegoHanabi.getJugadores();}


    // Método para obtener un jugador por su nombre y notificar al observador
    public void obtenerJugadorPorNombreParaPista(String nombre) {
        Jugador jugador = null;
        for (Jugador j : juegoHanabi.getJugadores()) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                jugador = j;
                break;
            }
        }

        if (jugador == null) {
            // Si no se encuentra el jugador, notificar al observador
            notificarObservador(Eventos.JUGADOR_NO_EXISTE, null);
        } else {
            // Notificar también para mostrar las cartas del jugador (opcional, dependiendo de cómo lo manejes)
            notificarObservador(Eventos.MOSTRAR_MANO_JUGADOR_PISTA, jugador);
        }
    }

   public void obtenerManoJugadorInstanciado(Jugador juegadorInstanciado){
       List<String> manoJugador = juegoMostrable.obtenerManoJugador(juegadorInstanciado);
       notificarObservador(Eventos.MOSTRAR_MANO, manoJugador);
   }
    public List<String> manoJugadorInicio(Jugador jugadorInstanciado) {
        List<String> manoJugador = juegoMostrable.obtenerManoJugador(jugadorInstanciado);
        return manoJugador;
    }

    public void manejarObtenerManosVisiblesResto(Jugador jugadorInstanciado) {
        // Obtener las manos visibles del resto de los jugadores desde JuegoMostrable
        List<Map<String, List<String>>> listaManosVisibles = juegoMostrable.obtenerManosVisiblesResto(jugadorInstanciado);

        // Notificar a las vistas con el evento correspondiente y el dato
        notificarObservador(Eventos.MOSTRAR_MANOS_VISIBLES_RESTO, listaManosVisibles);
    }

    public List<Map<String, List<String>>> manosRestoJugadoresInicio(Jugador jugadorInstanciado) {
        List<Map<String, List<String>>> listaManosVisibles = juegoMostrable.obtenerManosVisiblesResto(jugadorInstanciado);
        return listaManosVisibles;
    }



    public List<Object> obtenerDatosTablero() {
        List<Object> datosTablero = new ArrayList<>();

        // Obtener los diferentes datos del tablero y agregarlos a la lista
        datosTablero.add(juegoMostrable.obtenerCartasRestantesEnMazo());
        datosTablero.add(juegoMostrable.obtenerFichasDeVida());
        datosTablero.add(juegoMostrable.obtenerFichasDePistaUsadas());
        datosTablero.add(juegoMostrable.obtenerFichasDePista());
        datosTablero.add(juegoMostrable.obtenerCastillos());

        return datosTablero;
    }






    public void mostrarManoJugadorParaPista(Jugador jugador) {
        String manoPista = JuegoMostrable.mostrarManoJugador(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO_JUGADOR_PISTA, manoPista);
    }

    public void mostrarManoJugadorPorNumeroParaPista(Jugador jugador) {
        String manoNumeroPista = JuegoMostrable.mostrarManoJugadorPorNumero(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO_NUMEROS, manoNumeroPista);
    }

    public void mostrarManoJugadorPorColorParaPista(Jugador jugador) {
        String manoColorPista = JuegoMostrable.mostrarManoJugadorPorColor(jugador).toString();
        notificarObservador(Eventos.MOSTRAR_MANO_COLORES, manoColorPista);
    }



    public void registrarJugador(String nombreJugador) {
        try {
            juegoHanabi.registrarJugador(nombreJugador);
            notificarObservador(Eventos.JUGADOR_CREADO);
        } catch (IllegalArgumentException e) {
            notificarObservador(Eventos.ERROR_CREACION_JUGADOR, e.getMessage());
        }
    }
    public Jugador obtenerJugadorActual() {
        return juegoHanabi.getJugadorActual();

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