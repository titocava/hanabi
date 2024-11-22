package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;




public interface IVista {
    /**
     * Configura el controlador para la vista.
     *
     * @param controlador El controlador del juego.
     */
    void setControlador(ControladorJuegoHanabi controlador);

    /**
     * Muestra un mensaje en la vista.
     *
     * @param mensaje El mensaje a mostrar.
     */
    void mostrarMensaje(String mensaje);

    /**
     * Actualiza la vista con los cambios en el estado del juego según el evento recibido.
     *
     * @param evento El evento que indica qué ha cambiado.
     * @param data Datos adicionales relacionados con el evento (pueden ser objetos como
     *             cartas, jugadores, etc.).
     */
    void actualizar(Eventos evento, Object data);

    /**
     * Muestra la información del fin del juego.
     */
    void mostrarFinJuego();

    /**
     * Actualiza la cantidad de fichas de pista y de vida disponibles.
     *
     * @param fichasDePista Las fichas de pista disponibles.
     * @param fichasDeVida  Las fichas de vida restantes.
     */
    void actualizarFichas(int fichasDePista, int fichasDeVida);

    /**
     * Muestra el turno del jugador actual.
     *
     * @param jugador El jugador cuyo turno es.
     */
    void mostrarTurno(Jugador jugador);

    /**
     * Muestra la información de la mano del jugador actual.
     *
     * @param mano La mano del jugador.
     */
    void mostrarManoJugador(Jugador jugador);

    /**
     * Muestra el inicio del juego.
     */
    void mostrarInicioJuego();

    /**
     * Permite registrar un jugador.
     *
     * @param nombreJugador El nombre del jugador a registrar.
     */

}
