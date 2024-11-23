import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.JuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.JuegoMostrable;
import ar.edu.unlu.Hanabi.ModeloNew.Jugador;
import ar.edu.unlu.Hanabi.ModeloNew.Tablero;
import ar.edu.unlu.Hanabi.Vista.VistaConsolaGrafica;

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



            // Crear la vista (interfaz gráfica de consola) pasando el controlador
            VistaConsolaGrafica vista = new VistaConsolaGrafica(controlador);
            controlador.agregarObservador(vista);

            // Hacer visible la vista
            vista.setVisible(true);
        }
    }


