package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VistaConsolaGrafica extends JFrame implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final List<Jugador> jugadoresRegistrados;
    private Eventos eventos;



    public VistaConsolaGrafica(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
        this.jugadoresRegistrados = new ArrayList<>();
        this.txtSalida = new JTextArea();
        this.txtEntrada = new JTextField();
        this.btnEnter = new JButton("Enter");

        // Crea el JTextArea

// Configura el JTextArea para que no sea editable, y lo haga de solo lectura
        txtSalida.setEditable(false);

// Asegúrate de que el texto se ajuste bien
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true); // Ajuste de línea por palabra

// Agregar el JTextArea a un JScrollPane para desplazamiento si el texto es largo
        JScrollPane scrollPane = new JScrollPane(txtSalida);


        add(scrollPane, BorderLayout.CENTER);
        configurarVentana();
        configurarComponentes();
        configurarEventos();
        mostrarMenuPrincipal();
    }

    // Configuración general de la ventana principal
    private void configurarVentana() {
        setTitle("Hanabi - Juego en Consola");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // Inicialización y configuración de componentes gráficos
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

    // Configuración de eventos para interacción con los componentes
    private void configurarEventos() {
        btnEnter.addActionListener((ActionEvent e) -> procesarEntrada(txtEntrada.getText().trim()));
    }

    // Procesamiento de entradas según el estado actual
    // Procesamiento de entradas según el estado actual
    private void procesarEntrada(String entrada) {
        txtEntrada.setText(""); // Limpia el campo de entrada
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.");
            return;
        }

        switch (estado) {
            case MENU_PRINCIPAL -> procesarComandoMenuPrincipal(entrada);
            case REGISTRAR_JUGADOR -> procesarComandoRegistrarJugador(entrada);
            case NUEVA_PARTIDA -> ProcesarMenuNuevaPartida(entrada);
            case INICIAR_PARTIDA -> intentarIniciarJuego();


            default -> mostrarMensaje("Comando no reconocido.");
        }
    }

    // Menús y comandos para cada estado
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



    // Iniciar el registro de jugadores
    private void iniciarRegistroJugador() {

        if (jugadoresRegistrados.size() >= 5) {
            mostrarMensaje("Ya se ha alcanzado el límite máximo de jugadores.");
            return;
        }
        mostrarMensaje("Introduzca el nombre del jugador:");
        estado = EstadoVistaConsola.REGISTRAR_JUGADOR;

    }

    // Procesar el comando de registrar jugador
    private void procesarComandoRegistrarJugador(String nombre) {
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío.");
            return;
        }

        // Registrar el jugador a través del controlador
        controlador.registrarJugador(nombre);
    }

    private void intentarIniciarJuego() {

        List<Jugador> jugadores = controlador.obtenerListaJugadores();
        controlador.iniciarJuego(jugadores);
        controlador.iniciarTurno();
        mostrarMensaje("El juego ha comenzado. ¡Que comience la partida!");
        estado = EstadoVistaConsola.JUEGO_INICIADO;


    }

    private void crearVistasPorJugador() {
        // Obtener la lista de jugadores del controlador
        List<Jugador> jugadores = controlador.obtenerListaJugadores(); // Aquí obtenemos la lista de jugadores

        // Iterar sobre cada jugador
        for (Jugador jugador : jugadores) {
            // Crear una nueva instancia de VistaConsolaGraficaJugador, pasando el controlador y el jugador
            VistaConsolaGraficaJugador vistaJugador = new VistaConsolaGraficaJugador(controlador, jugador);

            // Registrar la vista como observador del controlador
            controlador.agregarObservador(vistaJugador);

            // Inicializar la vista solo una vez
            if (!vistaJugador.isVisible()) {
                vistaJugador.iniciar();

            }

            // Mostrar un mensaje indicando que se creó una vista para el jugador
            mostrarMensaje("Se creó una vista para el jugador: " + jugador.getNombre());
        }
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
                crearVistasPorJugador();
            }
            case JUGADOR_CREADO -> {
                mostrarMensaje("Jugador creado correctamente.");
                mostrarMenuNuevaPartida();
            }
            default -> mostrarMensaje("Evento desconocido: " + evento);
        }
    }

    @Override
    public void notificar(Eventos evento, Object data) {
        switch (evento) {


            default:
                mostrarMensaje("Evento desconocido." + evento);
                break;

    }
    }

}



