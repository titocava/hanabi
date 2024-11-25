import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import ar.edu.unlu.Hanabi.Vista.*;

import java.util.ArrayList;


    public class Main {
        public static void main(String[] args) {
            // Crear la lista de jugadores
            ArrayList<Jugador> jugadores = new ArrayList<>();

            // Crear el tablero de juego
            Tablero tablero = new Tablero(jugadores);  // Asumimos que el constructor de Tablero ya inicializa el tablero correctamente

            // Crear la instancia de JuegoHanabi (el juego en sí)
            JuegoHanabi juegoHanabi = new JuegoHanabi();  // Aquí deberías asegurarte de que se inicializan correctamente los elementos de JuegoHanabi

            // Crear la instancia de JuegoMostrable pasando JuegoHanabi
            JuegoMostrable juegoMostrable = new JuegoMostrable(juegoHanabi);

            // Crear el controlador pasando JuegoHanabi y JuegoMostrable
            ControladorJuegoHanabi controlador = new ControladorJuegoHanabi(juegoHanabi, juegoMostrable);



            /// Crear la vista usando la interfaz IVista
            IVista vista = new VistaConsolaGrafica(controlador);

            // Agregar la vista como observador al controlador
            controlador.agregarObservador((Observador) vista);
            vista.iniciar();
        }
    }


