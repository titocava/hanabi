package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VistaConsolaGrafica extends JFrame implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final List<Jugador> jugadoresRegistrados;

    public VistaConsolaGrafica(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
        this.jugadoresRegistrados = new ArrayList<>();
        this.txtSalida = new JTextArea();
        this.txtEntrada = new JTextField();
        this.btnEnter = new JButton("Enter");

        txtSalida.setEditable(false);

        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtSalida);


        add(scrollPane, BorderLayout.CENTER);
        configurarVentana();
        configurarComponentes();
        configurarEventos();
        mostrarMenuPrincipal();
    }

    private void configurarVentana() {
        setTitle("Hanabi - Juego en Consola");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void configurarComponentes() {
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtSalida);
        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.add(txtEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnter, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos()  {
        // Configura el evento de clic en el botón
        btnEnter.addActionListener((ActionEvent e) -> procesarEntrada(txtEntrada.getText().trim()));

        // Configura el evento de presionar la tecla Enter en el campo de texto
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    // Simula el clic en el botón "Enter" cuando se presiona Enter
                    btnEnter.doClick();
                    e.consume();  // Evita que se añadan saltos de línea al presionar Enter
                }
            }
        });
    }


    private void procesarEntrada(String entrada) {
        txtEntrada.setText("");
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.");
            return;
        }

        switch (estado) {
            case MENU_PRINCIPAL -> procesarComandoMenuPrincipal(entrada);
            case REGISTRAR_JUGADOR -> procesarComandoRegistrarJugador(entrada);
            case NUEVA_PARTIDA -> ProcesarMenuNuevaPartida(entrada);
            case INICIAR_PARTIDA -> intentarIniciarJuego();
            case NUEVO_JUGADOR -> procesarComandoNuevoJugador(entrada);
            default -> mostrarMensaje("Comando no reconocido.");
        }
    }

    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        mostrarMensaje("""
            Menú Principal:
            1. Nueva Partida
            2. Iniciar Partida
            3. Salir
            Seleccione una opción:
            """);
    }

    private void procesarComandoMenuPrincipal(String comando) {
        switch (comando) {
            case "1" -> mostrarMenuNuevaPartida();
            case "2" -> intentarIniciarJuego();
            case "3" -> salirJuego();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2' o '3'.");
        }
    }

    private void mostrarMenuNuevaPartida() {
        estado = EstadoVistaConsola.NUEVA_PARTIDA;
        mostrarMensaje("""
        Menú Ingreso de Jugadores:
        1. Crear un nuevo jugador
        2. Iniciar partida
        Seleccione una opción:
        """);
    }

    private void ProcesarMenuNuevaPartida(String comando) {
        switch (comando) {
            case "1" -> iniciarRegistroJugador(); // Llama a los métodos ya implementados para registrar jugadores
            case "2" -> intentarIniciarJuego();   // Valida que haya suficientes jugadores
            default -> mostrarMensaje("Opción no válida. Elija '1' o '2'.");
        }
    }

    private void mostrarMenuNuevoJugador() {
        estado = EstadoVistaConsola.NUEVO_JUGADOR;
        mostrarMensaje("""
        ¿Deseas crear un nuevo jugador?
        1. Sí, crear un nuevo jugador.
        2. No, volver al menú de nueva partida.
        Selecciona una opción:
    """);
    }

    private void procesarComandoNuevoJugador(String comando) {
        switch (comando) {
            case "1" -> iniciarRegistroJugador(); // Si elige "1", registra un nuevo jugador
            case "2" -> intentarIniciarJuego(); // Si elige "2", vuelve al menú de nueva partida
            default -> mostrarMensaje("Opción no válida. Elija '1' o '2'.");
        }
    }

    private void iniciarRegistroJugador() {
        if (jugadoresRegistrados.size() >= 5) {
            mostrarMensaje("Ya se ha alcanzado el límite máximo de jugadores.");
            return;
        }
        mostrarMensaje("Introduzca el nombre del jugador:");
        estado = EstadoVistaConsola.REGISTRAR_JUGADOR;

    }

    private void procesarComandoRegistrarJugador(String nombre) {
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío.");
            return;
        }
        controlador.registrarJugador(nombre);
    }

    private void intentarIniciarJuego() {

        List<Jugador> jugadores = controlador.obtenerListaJugadores();
        controlador.iniciarJuego(jugadores);
        controlador.iniciarTurno();
        mostrarMensaje("El juego ha comenzado. ¡Que comience la partida!");
        //estado = EstadoVistaConsola.JUEGO_INICIADO;

    }

    private void crearVistaPorJugador(Jugador jugador) {
        VistaConsolaGraficaJugador vistaJugador = new VistaConsolaGraficaJugador(controlador, jugador);
        controlador.agregarObservador(vistaJugador);
        vistaJugador.iniciar();
        mostrarMensaje("Se creó una vista para el jugador: " + jugador.getNombre());
    }

    private void salirJuego() {
        mostrarMensaje("Saliendo del juego.");
        System.exit(0);
    }

    @Override
    public void iniciar() {
        setVisible(true); // Aquí se asegura la visibilidad
    }


    // Métodos de IVista
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
        System.out.println("Evento recibido: " + evento); // Para depuración
        switch (evento) {
            case INICIAR_JUEGO -> {
                mostrarMensaje("¡Es el turno de " + controlador.obtenerJugadorActual().getNombre() + "!");

            }

            default -> mostrarMensaje("Evento desconocido: " + evento);
        }
    }

    @Override
    public void notificar(Eventos evento, Object data) {
        switch (evento) {
            case JUGADOR_CREADO -> {
                mostrarMensaje("Jugador creado correctamente.");

                if (data instanceof Jugador jugador) {
                    crearVistaPorJugador(jugador);
                } else {
                    mostrarMensaje("Error: El objeto recibido no es un jugador válido.");
                }
                mostrarMenuNuevoJugador();
            }

            default -> mostrarMensaje("Evento desconocido: " + evento);
        }
    }


}



