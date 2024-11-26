package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Consumer;


public class SeleccionJugador {
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;

    public SeleccionJugador(JTextArea txtSalida, JTextField txtEntrada, JButton btnEnter) {
        this.txtSalida = txtSalida;
        this.txtEntrada = txtEntrada;
        this.btnEnter = btnEnter;
    }

    public void seleccionar(List<Jugador> jugadores, Consumer<Jugador> callback)   {
        if (jugadores.isEmpty()) {
            mostrarMensaje("No hay jugadores disponibles para seleccionar.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("Seleccione un jugador:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            mensaje.append(i + 1).append(". ").append(jugadores.get(i).getNombre()).append("\n");
        }
        mostrarMensaje(mensaje.toString());

        configurarListenerDinamico(e -> {
            try {
                int seleccion = Integer.parseInt(txtEntrada.getText().trim()) - 1;
                txtEntrada.setText("");
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

    private void configurarListenerDinamico(ActionListener listener){
        for (ActionListener al : btnEnter.getActionListeners()) {
            btnEnter.removeActionListener(al);
        }
        btnEnter.addActionListener(listener);
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnEnter.doClick();
                    e.consume();
                }
            }
        });

        txtEntrada.requestFocusInWindow();
    }

    private void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }
}
