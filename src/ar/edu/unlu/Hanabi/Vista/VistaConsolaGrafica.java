package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VistaConsolaGrafica extends JFrame implements IVista {
    private ControladorJuegoHanabi controlador;
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
    private void procesarEntrada(String entrada) {
        txtEntrada.setText(""); // Limpia el campo de entrada
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.");
            return;
        }

        switch (estado) {
            case MENU_PRINCIPAL -> procesarComandoMenuPrincipal(entrada);
            case NUEVA_PARTIDA -> procesarComandoNuevaPartida(entrada);
            case REGISTRAR_JUGADOR -> procesarComandoRegistrarJugador(entrada);
            case INICIAR_PARTIDA -> procesarComandoIniciarPartida(entrada);
            case MENU_ACCIONES_JUGADOR -> procesarComandoMenuAccionesJugador(entrada);
            case JUGAR_CARTA -> procesarComandoJugarCarta(entrada);
            case DESCARTAR_CARTA -> procesarComandoDescartarCarta(entrada);
            case DAR_PISTA -> procesarComandoDarPista(entrada);
            default -> mostrarMensaje("Comando no reconocido.");
        }
    }

    // Menús y comandos para cada estado
    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        mostrarMensaje("""
                Menú Principal:
                1. Nueva Partida
                2. Salir
                Seleccione una opción:
                """);
    }

    private void procesarComandoMenuPrincipal(String comando) {
        switch (comando) {
            case "1" -> mostrarMenuNuevaPartida();
            case "2" -> salirJuego();
            default -> mostrarMensaje("Opción no válida. Elija '1' para Nueva Partida o '2' para Salir.");
        }
    }

    private void mostrarMenuNuevaPartida() {
        estado = EstadoVistaConsola.NUEVA_PARTIDA;
        mostrarMensaje("""
                Menú Nueva Partida:
                1. Registrar Jugador
                2. Iniciar Juego
                Seleccione una opción:
                """);
    }

    private void procesarComandoNuevaPartida(String comando) {
        switch (comando) {
            case "1" -> iniciarRegistroJugador();
            case "2" -> intentarIniciarJuego();
            default -> mostrarMensaje("Opción no válida. Intente nuevamente.");
        }
    }

    private void iniciarRegistroJugador() {
        estado = EstadoVistaConsola.REGISTRAR_JUGADOR;
        mostrarMensaje("Introduzca el nombre del jugador:");
    }

    private void procesarComandoRegistrarJugador(String nombre) {
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío.");
            return;
        }

        if (jugadoresRegistrados.size() >= 4) {
            mostrarMensaje("Ya se ha alcanzado el límite máximo de jugadores.");
            // Aquí verificamos si podemos continuar al menú de iniciar partida
            mostrarMenuNuevaPartida(); // Volver a mostrar la opción para iniciar juego
            return;
        }

        Jugador nuevoJugador = controlador.crearJugador(nombre);
        if (nuevoJugador != null) {
            jugadoresRegistrados.add(nuevoJugador);
            mostrarMensaje("Jugador '" + nombre + "' registrado.");
        } else {
            mostrarMensaje("Error al registrar el jugador. Intente nuevamente.");
        }

        if (jugadoresRegistrados.size() >= 2) {
            mostrarMensaje("Se han registrado suficientes jugadores. Puede iniciar el juego.");
            mostrarMenuNuevaPartida(); // Volver al menú para iniciar el juego
        } else {
            // Si no se alcanzan los jugadores mínimos, seguimos en el registro
            mostrarMensaje("Aún necesita " + (2 - jugadoresRegistrados.size()) + " jugadores más.");
        }
    }

    private void intentarIniciarJuego() {
        if (jugadoresRegistrados.size() < 2) {
            mostrarMensaje("Se necesitan al menos 2 jugadores para iniciar el juego.");
            return;
        }
        controlador.iniciarJuego(jugadoresRegistrados);
        mostrarMensaje("El juego ha comenzado. ¡Que comience la partida!");
        estado = EstadoVistaConsola.JUEGO_EN_CURSO;
    }

    private void procesarComandoMenuAccionesJugador(String comando) {
        switch (comando) {
            case "1" -> estado = EstadoVistaConsola.JUGAR_CARTA;
            case "2" -> estado = EstadoVistaConsola.DESCARTAR_CARTA;
            case "3" -> estado = EstadoVistaConsola.DAR_PISTA;
            default -> mostrarMensaje("Opción no válida.");
        }
    }

    private void salirJuego() {
        mostrarMensaje("Saliendo del juego.");
        System.exit(0);
    }

    private void procesarComandoIniciarPartida(String comando) {
        if (jugadoresRegistrados.size() < 2) {
            mostrarMensaje("No hay suficientes jugadores. Se requieren al menos 2 para iniciar la partida.");
            return;
        }
        controlador.iniciarJuego(jugadoresRegistrados);
        mostrarMensaje("La partida ha comenzado. ¡Buena suerte!");
        estado = EstadoVistaConsola.MENU_ACCIONES_JUGADOR; // Cambiar al menú de acciones del jugador actual
        controlador.mostrarManoJugadorTurno(controlador.obtenerJugadorActual()); // Asume que este método existe
    }

    private void procesarComandoJugarCarta(String entrada) {
        try {
            int index = Integer.parseInt(entrada.trim()) - 1; // Convertir entrada a índice
            Jugador jugadorActual = controlador.obtenerJugadorActual(); // Obtener jugador actual
            List<Carta> mano = jugadorActual.getMano(); // Obtener mano del jugador
            if (index >= 0 && index < mano.size()) {
                Carta cartaAJugar = mano.get(index);
                controlador.jugadorJuegaCarta(jugadorActual, cartaAJugar); // Enviar al controlador
            } else {
                mostrarMensaje("Índice no válido. Intente nuevamente.");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Por favor, ingrese un número válido.");
        }
    }

    private void procesarComandoDescartarCarta(String entrada) {
        try {
            int index = Integer.parseInt(entrada.trim()) - 1;
            Jugador jugadorActual = controlador.obtenerJugadorActual();
            List<Carta> mano = jugadorActual.getMano();
            if (index >= 0 && index < mano.size()) {
                Carta cartaADescartar = mano.get(index);
                controlador.jugadorDescartaCarta(jugadorActual, cartaADescartar);
            } else {
                mostrarMensaje("Índice no válido. Intente nuevamente.");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Por favor, ingrese un número válido.");
        }
    }

    private void procesarComandoDarPista(String entrada) {
        String[] partes = entrada.split("\\s+");

        // Verificamos que la entrada sea válida (al menos 2 partes: nombre y tipo de pista)
        if (partes.length < 2) {
            mostrarMensaje("Formato incorrecto. Use: [Jugador] [color|número]");
            return;
        }

        String nombreJugador = partes[0];
        String tipoPista = partes[1].toLowerCase();

        // Obtenemos el jugador objetivo
        Jugador jugadorObjetivo = controlador.obtenerJugadorPorNombre(nombreJugador);
        if (jugadorObjetivo == null) {
            mostrarMensaje("Jugador no encontrado. Intente nuevamente.");
            return;
        }

        // Ahora creamos la pista según el tipo de pista
        TipoPista tipoPistaEnum;
        Pista pista = null;
        if (tipoPista.equals("color")) {
            // Aquí debes obtener un color (de alguna forma, dependiendo de tu implementación de ColorCarta)
            // Supondré que obtienes el color de alguna forma (por ejemplo, por consola o seleccionando un color predefinido).
            mostrarMensaje("Por favor, ingrese el color de la pista (como ColorCarta)");
            String colorInput = obtenerEntradaColor();  // Método que debería implementar para obtener el color
            ColorCarta color = ColorCarta.valueOf(colorInput.toUpperCase());  // Suponiendo que ColorCarta es un enum
            tipoPistaEnum = TipoPista.COLOR;
            pista = controlador.crearPista(tipoPistaEnum, color);
        } else if (tipoPista.equals("número")) {
            // En este caso, obtenemos el número para la pista
            mostrarMensaje("Por favor, ingrese el número de la pista.");
            String numeroInput = obtenerEntradaNumero();  // Método que debería implementar para obtener el número
            Integer numero = Integer.valueOf(numeroInput);
            tipoPistaEnum = TipoPista.NUMERO;
            pista = controlador.crearPista(tipoPistaEnum, numero);
        } else {
            mostrarMensaje("Tipo de pista no válido. Use 'color' o 'número'.");
            return;
        }

        // Finalmente, notificar al controlador que se ha dado una pista
        controlador.jugadorDaPista(controlador.obtenerJugadorActual(), jugadorObjetivo, pista);
        mostrarMensaje("Pista dada a " + jugadorObjetivo.getNombre() + ": " + tipoPista);
    }
    // Método para obtener la entrada de color (supuesto que se usa un enum ColorCarta)
    private String obtenerEntradaColor() {
        // Aquí deberías implementar la lógica para pedir al usuario que ingrese un color
        return "ROJO";  // Esto es solo un ejemplo, deberías pedir realmente al usuario
    }

    // Método para obtener la entrada de número
    private String obtenerEntradaNumero() {
        // Aquí deberías implementar la lógica para pedir al usuario que ingrese un número
        return "5";  // Esto es solo un ejemplo, deberías pedir realmente al usuario
    }


    private void mostrarManoJugadorTurno() {
        Jugador jugadorActual = controlador.obtenerJugadorActual();
        mostrarMensaje("Es el turno de: " + jugadorActual.getNombre());

        // Mostrar la mano del jugador
        mostrarMensaje("Tu mano:");
        List<Carta> mano = jugadorActual.getMano();
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            mostrarMensaje((i + 1) + ". " + carta);  // Mostrar carta y su índice
        }

        // Mostrar las opciones disponibles
        mostrarMenuAccionesJugador();
    }

    private void mostrarMenuAccionesJugador() {
        Jugador jugadorActual = controlador.obtenerJugadorActual(); // Obtener al jugador actual
        mostrarMensaje("Es el turno de: " + jugadorActual.getNombre());

        // Mostrar la mano del jugador
        mostrarMensaje("Tu mano:");
        List<Carta> mano = jugadorActual.getMano();
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            mostrarMensaje((i + 1) + ". " + carta);  // Mostrar la carta y su índice
        }

        // Mostrar las opciones del menú
        mostrarMensaje("""
            Menú de Acciones:
            1. Jugar una carta
            2. Descartar una carta
            3. Dar una pista
            4. Finalizar turno
            Seleccione una opción:
            """);
    }




    // Métodos de IVista
    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public void setControlador(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
    }

    @Override
    public void actualizar(Eventos evento, Object data) {
        // Implementar según los eventos que maneje el juego
    }

    @Override
    public void mostrarFinJuego() {
        mostrarMensaje("¡El juego ha terminado!");
    }

    @Override
    public void actualizarFichas(int fichasDePista, int fichasDeVida) {
        mostrarMensaje("Fichas de pista disponibles: " + fichasDePista);
        mostrarMensaje("Fichas de vida restantes: " + fichasDeVida);
    }

    @Override
    public void mostrarTurno(Jugador jugador) {
        mostrarMensaje("Es el turno de: " + jugador.getNombre());
    }

    @Override
    public void mostrarManoJugador(Jugador jugador) {
        // Implementación futura
    }

    @Override
    public void mostrarInicioJuego() {
        // Implementación futura
    }
}

