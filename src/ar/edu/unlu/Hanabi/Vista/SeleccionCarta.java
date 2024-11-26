package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SeleccionCarta {
    private JTextArea txtSalida;
    private JTextField txtEntrada;
    private JButton btnEnter;
    private ActionListener currentListener; // Consider making this a field

    public SeleccionCarta(JTextArea txtSalida, JTextField txtEntrada, JButton btnEnter) {
        this.txtSalida = txtSalida;
        this.txtEntrada = txtEntrada;
        this.btnEnter = btnEnter;

        // Configurar listener inicial una sola vez
        configurarListenerInicial();
    }

    private void configurarListenerInicial()  {
        // Usar una expresión lambda vacía o un método separado
        currentListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Listener base vacío
            }
        };
        btnEnter.addActionListener(currentListener);

        // Listener para tecla Enter
        txtEntrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnEnter.doClick();
                    e.consume();
                }
            }
        });
    }

    public void seleccionar(List<Carta> mano, Consumer<Carta> callback) {
        // Eliminar listeners anteriores de manera segura
        ActionListener[] listeners = btnEnter.getActionListeners();
        for (ActionListener listener : listeners) {
            btnEnter.removeActionListener(listener);
        }

        if (mano.isEmpty()) {
            mostrarMensaje("No hay cartas en la mano para seleccionar.");
            return;
        }

        // Preparar el mensaje de selección
        StringBuilder mensaje = new StringBuilder("Seleccione una carta de su mano:\n");
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            mensaje.append(i + 1).append(". ").append(carta.esRevelada() ? carta.toString() : "Carta no visible").append("\n");
        }
        mostrarMensaje(mensaje.toString());

        // Flag para evitar mensajes repetidos
        AtomicBoolean mensajeEnviado = new AtomicBoolean(false);

        // Crear un listener específico para esta selección
        ActionListener seleccionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String entrada = txtEntrada.getText().trim();
                    txtEntrada.setText("");

                    if (entrada.isEmpty()) {
                        // Solo mostrar el mensaje si no se ha enviado antes
                        if (!mensajeEnviado.getAndSet(true)) {
                            mostrarMensaje("Por favor, ingrese un número para seleccionar una carta.");
                        }
                        return;
                    }

                    // Resetear el flag cuando se ingresa algo
                    mensajeEnviado.set(false);

                    int seleccion = Integer.parseInt(entrada) - 1;

                    if (seleccion >= 0 && seleccion < mano.size()) {
                        Carta cartaSeleccionada = mano.get(seleccion);
                        mostrarMensaje("Has seleccionado: " + cartaSeleccionada.toString());

                        // Importante: eliminar el listener después de la selección
                        btnEnter.removeActionListener(this);

                        callback.accept(cartaSeleccionada);
                    } else {
                        mostrarMensaje("Selección inválida. Intente nuevamente.");
                    }
                } catch (NumberFormatException ex) {
                    mostrarMensaje("Entrada no válida. Por favor, ingrese un número.");
                }
            }
        };

        // Agregar el listener
        btnEnter.addActionListener(seleccionListener);
        txtEntrada.requestFocusInWindow();
    }

    private void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }
}




