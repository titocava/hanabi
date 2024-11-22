package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final List<Carta> mano; // Cartas en la mano del jugador
    private boolean turno;          // Indica si es el turno del jugador
    private String nombre;          // Nombre del jugador

    // Constructor
    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        this.nombre = nombre.trim();  // Elimina espacios en blanco al inicio y al final
        this.mano = new ArrayList<>();  // Inicializa la lista de cartas
        this.turno = false;            // Por defecto, no tiene el turno
    }

    // Métodos

    /**
     * Agrega una carta a la mano del jugador.
     *
     * @param carta la carta que se agrega a la mano
     */
    public void agregarCartaACartasEnMano(Carta carta) {
        if (carta != null) {
            mano.add(carta);
        }
    }

    /**
     * Elimina una carta específica de la mano del jugador.
     *
     * @param carta la carta que se elimina
     * @return true si la carta se eliminó correctamente; false si la carta no estaba en la mano
     */
    public boolean eliminarCartaDeLaMano(Carta carta) {
        return mano.remove(carta);
    }

    /**
     * Devuelve las cartas actuales en la mano del jugador como un arreglo.
     *
     * @return un arreglo con las cartas en la mano
     */
    public List<Carta> getMano() {
        return mano; // Devuelve la lista directamente
    }

    /**
     * Establece si el jugador tiene el turno actual.
     *
     * @param turno true si es el turno del jugador, false en caso contrario
     */
    public void setJugadorTurno(boolean turno) {
        this.turno = turno;
    }

    /**
     * Verifica si es el turno actual del jugador.
     *
     * @return true si es el turno del jugador, false en caso contrario
     */
    public boolean tieneTurno() {
        return turno;
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return el nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece un nuevo nombre para el jugador.
     *
     * @param nombre el nuevo nombre del jugador
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Devuelve el número de cartas en la mano del jugador.
     *
     * @return el número de cartas en la mano
     */
    public int obtenerCantidadDeCartasEnMano() {
        return mano.size();
    }
}

