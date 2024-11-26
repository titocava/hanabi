package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Consumer;

public class SeleccionJugadorGrafico extends JPanel {
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;

    // Constructor que recibe los componentes necesarios
    public SeleccionJugadorGrafico(JTextArea txtSalida, JTextField txtEntrada, JButton btnEnter) {
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

    // Método para seleccionar un jugador
    public void seleccionar(List<Jugador> jugadores, Consumer<Jugador> callback) {
        if (jugadores.isEmpty()) {
            mostrarMensaje("No hay jugadores disponibles para seleccionar.");
            return;
        }

        // Mostrar la lista de jugadores
        StringBuilder mensaje = new StringBuilder("Seleccione un jugador:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            mensaje.append(i + 1).append(". ").append(jugadores.get(i).getNombre()).append("\n");
        }
        mostrarMensaje(mensaje.toString());

        // Configurar el listener para el botón de confirmar
        configurarListenerDinamico(e -> {
            try {
                int seleccion = Integer.parseInt(txtEntrada.getText().trim()) - 1;
                txtEntrada.setText(""); // Limpiar la entrada

                if (seleccion >= 0 && seleccion < jugadores.size()) {
                    Jugador jugadorSeleccionado = jugadores.get(seleccion);
                    mostrarMensaje("Has seleccionado: " + jugadorSeleccionado.getNombre());
                    callback.accept(jugadorSeleccionado);
                } else {
                    mostrarMensaje("Selección inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Entrada no válida. Por favor, ingrese un número.");
            }
        });
    }

    // Configurar el listener dinámico para el botón de confirmar
    private void configurarListenerDinamico(ActionListener listener) {
        for (ActionListener al : btnEnter.getActionListeners()) {
            btnEnter.removeActionListener(al);
        }
        btnEnter.addActionListener(listener);

        // Permitir que el usuario presione "Enter" para confirmar la selección
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnEnter.doClick(); // Simula un clic en el botón "Confirmar"
                    e.consume(); // Evitar que se añadan saltos de línea
                }
            }
        });

        txtEntrada.requestFocusInWindow(); // Enfocar en el campo de texto
    }

    // Método para mostrar mensajes en el JTextArea
    private void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }
}
