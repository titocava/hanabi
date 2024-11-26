package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VistaGrafica extends JFrame implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private EstadoVistaConsola estado;
    private final List<Jugador> jugadoresRegistrados;

    public VistaGrafica(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
        this.jugadoresRegistrados = new ArrayList<>();
        this.txtSalida = new JTextArea();

        configurarVentana();
        configurarComponentes();
        mostrarMenuPrincipal();
    }

    private void configurarVentana() {
        setTitle("Hanabi - Juego Gráfico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void configurarComponentes() {

        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtSalida);

        // Agregar el JScrollPane al contenedor
        add(scrollPane, BorderLayout.CENTER);
    }

    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        mostrarOpciones("Menú Principal", new String[]{
                "Nueva Partida",
                "Iniciar Partida",
                "Salir"
        }, this::procesarComandoMenuPrincipal);
    }

    private void procesarComandoMenuPrincipal(int opcion) {
        switch (opcion) {
            case 0 -> mostrarMenuNuevaPartida();
            case 1 -> intentarIniciarJuego();
            case 2 -> salirJuego();
            default -> mostrarMensaje("Opción no válida.");
        }
    }

    private void mostrarMenuNuevaPartida() {
        estado = EstadoVistaConsola.NUEVA_PARTIDA;
        mostrarOpciones("Menú de Nueva Partida", new String[]{
                "Crear un nuevo jugador",
                "Iniciar partida"
        }, this::procesarMenuNuevaPartida);
    }

    private void procesarMenuNuevaPartida(int opcion) {
        switch (opcion) {
            case 0 -> iniciarRegistroJugador();
            case 1 -> intentarIniciarJuego();
            default -> mostrarMensaje("Opción no válida.");
        }
    }

    private void iniciarRegistroJugador() {
        if (jugadoresRegistrados.size() >= 5) {
            mostrarMensaje("Ya se alcanzó el límite máximo de jugadores.");
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del jugador:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            controlador.registrarJugador(nombre.trim());
        } else {
            mostrarMensaje("El nombre no puede estar vacío.");
        }
    }

    private void intentarIniciarJuego() {
        List<Jugador> jugadores = controlador.obtenerListaJugadores();
        if (jugadores.size() < 2) {
            mostrarMensaje("Se necesitan al menos 2 jugadores para iniciar la partida.");
            mostrarMenuNuevaPartida();
            return;
        }
        controlador.iniciarJuego(jugadores);
        controlador.iniciarTurno();
        mostrarMensaje("El juego ha comenzado.");
        estado = EstadoVistaConsola.JUEGO_INICIADO;
    }

    private void salirJuego() {
        mostrarMensaje("Saliendo del juego...");
        System.exit(0);
    }

    private void mostrarOpciones(String titulo, String[] opciones, OpcionSeleccionada callback) {
        JPanel panel = new JPanel(new GridLayout(opciones.length + 1, 1));
        panel.add(new JLabel(titulo, SwingConstants.CENTER));
        ButtonGroup grupoBotones = new ButtonGroup();
        JRadioButton[] botones = new JRadioButton[opciones.length];

        for (int i = 0; i < opciones.length; i++) {
            botones[i] = new JRadioButton(opciones[i]);
            grupoBotones.add(botones[i]);
            panel.add(botones[i]);
        }

        int opcionSeleccionada = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcionSeleccionada == JOptionPane.OK_OPTION) {
            for (int i = 0; i < botones.length; i++) {
                if (botones[i].isSelected()) {
                    callback.seleccionarOpcion(i);
                    return;
                }
            }
            mostrarMensaje("Por favor, seleccione una opción.");
        }
    }

    // Métodos de IVista
    @Override
    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public void mostrarFinJuego() {
        mostrarMensaje("¡El juego ha terminado!");
    }

    @Override
    public void notificar(Eventos evento) {
        switch (evento) {
            case INICIAR_JUEGO -> mostrarMensaje("¡Comienza la partida!");
            case JUGADOR_CREADO -> mostrarMensaje("Jugador registrado con éxito.");
            default -> mostrarMensaje("Evento no manejado: " + evento);
        }
    }

    @Override
    public void notificar(Eventos evento, Object data) {
        mostrarMensaje("Evento con datos: " + evento + " | Datos: " + data);
    }

    // Interfaz funcional para opciones
    @FunctionalInterface
    interface OpcionSeleccionada {
        void seleccionarOpcion(int opcion);
    }
}
