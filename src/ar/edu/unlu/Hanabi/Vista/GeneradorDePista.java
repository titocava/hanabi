package ar.edu.unlu.Hanabi.Vista;
import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Consumer;


public class GeneradorDePista {

    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private final ControladorJuegoHanabi controlador;

    public GeneradorDePista(JTextArea txtSalida, JTextField txtEntrada, JButton btnEnter, ControladorJuegoHanabi controlador)  {
        this.txtSalida = txtSalida;
        this.txtEntrada = txtEntrada;
        this.btnEnter = btnEnter;
        this.controlador = controlador;
    }

    // Método principal que inicia el proceso
    public void generar(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        mostrarManoJugador(jugadorSeleccionado);
        mostrarMensaje("Seleccione el tipo de pista:\n1. Número\n2. Color");
        configurarListenerDinamico(event -> {
            String opcion = txtEntrada.getText().trim();
            txtEntrada.setText("");
            switch (opcion) {
                case "1" -> seleccionarNumeroPista(jugadorSeleccionado, callback);
                case "2" -> seleccionarColorPista(jugadorSeleccionado, callback);
                default -> mostrarMensaje("Opción no válida. Intente nuevamente.");
            }
        });
    }

    private void mostrarManoJugador(Jugador jugadorSeleccionado) {
        List<Carta> manoJugador = controlador.obtenerManoJugadorVisible(jugadorSeleccionado);
        StringBuilder mensaje = new StringBuilder("Mano del jugador (todas visibles):\n");

        for (int i = 0; i < manoJugador.size(); i++) {
            Carta carta = manoJugador.get(i);
            mensaje.append(i + 1).append(". ").append(carta.toString()).append("\n");
        }

        mostrarMensaje(mensaje.toString());
    }

    private void mostrarManoPorNumero(Jugador jugadorSeleccionado) {
        List<Carta> manoJugador = controlador.obtenerManoJugadorVisible(jugadorSeleccionado);
        StringBuilder mensaje = new StringBuilder("Cartas con números:\n");

        for (int i = 0; i < manoJugador.size(); i++) {
            Carta carta = manoJugador.get(i);
            mensaje.append(i + 1).append(". ").append(carta.toString()).append("\n");
        }

        mostrarMensaje(mensaje.toString());
    }

    private void mostrarManoPorColor(Jugador jugadorSeleccionado) {
        List<Carta> manoJugador = controlador.obtenerManoJugadorVisible(jugadorSeleccionado);
        StringBuilder mensaje = new StringBuilder("Cartas con colores:\n");

        for (int i = 0; i < manoJugador.size(); i++) {
            Carta carta = manoJugador.get(i);
            mensaje.append(i + 1).append(". ").append(carta.toString()).append("\n");
        }

        mostrarMensaje(mensaje.toString());
    }

    // Método para seleccionar un número para la pista
    private void seleccionarNumeroPista(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        mostrarManoPorNumero(jugadorSeleccionado);  // Mostrar las cartas filtradas por número

        mostrarMensaje("Seleccione el número para la pista:");

        configurarListenerDinamico(e -> {
            String valorSeleccionado = txtEntrada.getText().trim();
            txtEntrada.setText("");

            try {
                int valor = Integer.parseInt(valorSeleccionado);
                Pista pista = controlador.crearPista(TipoPista.NUMERO, valor);
                callback.accept(pista);  // Devolver la pista creada
            } catch (NumberFormatException ex) {
                mostrarMensaje("Valor no válido. Intente nuevamente.");
            }
        });
    }

    private void seleccionarColorPista(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        mostrarManoPorColor(jugadorSeleccionado);
        mostrarMensaje("Seleccione el color para la pista:");

        configurarListenerDinamico(e -> {
            String valorSeleccionado = txtEntrada.getText().trim();
            txtEntrada.setText("");

            try {
                ColorCarta valor = ColorCarta.valueOf(valorSeleccionado.toUpperCase());
                Pista pista = controlador.crearPista(TipoPista.COLOR, valor);
                callback.accept(pista);
            } catch (IllegalArgumentException ex) {
                mostrarMensaje("Valor no válido. Intente nuevamente.");
            }
        });
    }

    private void configurarListenerDinamico(ActionListener nuevoListener) {
        for (ActionListener listener : btnEnter.getActionListeners()) {
            btnEnter.removeActionListener(listener);
        }
        btnEnter.addActionListener(nuevoListener);
        txtEntrada.requestFocusInWindow();
    }

    private void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }
}

