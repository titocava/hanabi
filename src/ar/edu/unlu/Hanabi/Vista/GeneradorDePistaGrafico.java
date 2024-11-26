package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Consumer;

public class GeneradorDePistaGrafico extends JPanel {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;

    public GeneradorDePistaGrafico(JTextArea txtSalida, JTextField txtEntrada, JButton btnEnter, ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
        this.txtSalida = txtSalida;
        this.txtEntrada = txtEntrada;
        this.btnEnter = btnEnter;
        configurarPanel();
    }

    // Configura el panel con los componentes
    private void configurarPanel()  {
        this.setLayout(new BorderLayout());

        // Configurar el área de salida
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);
        JScrollPane scrollSalida = new JScrollPane(txtSalida);
        this.add(scrollSalida, BorderLayout.CENTER);

        // Configuración del panel inferior con el campo de texto y el botón
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(txtEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnter, BorderLayout.EAST);
        this.add(panelInferior, BorderLayout.SOUTH);
    }

    public void generar(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        mostrarManoJugador(jugadorSeleccionado);

        // Solicitar al usuario el tipo de pista
        String[] opciones = {"Número", "Color"};
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el tipo de pista:",
                "Generar Pista",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if ("Número".equals(seleccion)) {
            seleccionarNumeroPista(jugadorSeleccionado, callback);
        } else if ("Color".equals(seleccion)) {
            seleccionarColorPista(jugadorSeleccionado, callback);
        }
    }

    // Mostrar la mano del jugador
    private void mostrarManoJugador(Jugador jugadorSeleccionado) {
        List<Carta> mano = controlador.obtenerManoJugadorVisible(jugadorSeleccionado);
        StringBuilder mensaje = new StringBuilder("Mano del jugador (todas visibles):\n");

        for (Carta carta : mano) {
            mensaje.append("- ").append(carta.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(this, mensaje.toString(), "Mano del Jugador", JOptionPane.INFORMATION_MESSAGE);
    }

    // Seleccionar la pista de número
    private void seleccionarNumeroPista(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        String numero = JOptionPane.showInputDialog(this, "Ingrese el número para la pista:", "Seleccionar Número", JOptionPane.PLAIN_MESSAGE);
        try {
            int valor = Integer.parseInt(numero);
            Pista pista = controlador.crearPista(TipoPista.NUMERO, valor);
            callback.accept(pista);
            JOptionPane.showMessageDialog(this, "Pista creada: Número " + valor, "Pista Generada", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número no válido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Seleccionar la pista de color
    private void seleccionarColorPista(Jugador jugadorSeleccionado, Consumer<Pista> callback) {
        String color = JOptionPane.showInputDialog(this, "Ingrese el color para la pista (ej. ROJO):", "Seleccionar Color", JOptionPane.PLAIN_MESSAGE);
        try {
            ColorCarta valor = ColorCarta.valueOf(color.toUpperCase());
            Pista pista = controlador.crearPista(TipoPista.COLOR, valor);
            callback.accept(pista);
            JOptionPane.showMessageDialog(this, "Pista creada: Color " + valor, "Pista Generada", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Color no válido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
