package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import java.util.List;


public interface IVista {

    void setControlador(ControladorJuegoHanabi controlador);


    void mostrarMensaje(String mensaje);


    void actualizar(Eventos evento, Object data);


    void mostrarFinJuego();


    void actualizarFichas(int fichasDePista, int fichasDeVida);


    void mostrarTurno(Jugador jugador);


    void mostrarManoJugador(Jugador jugador);


    void mostrarInicioJuego();





}
