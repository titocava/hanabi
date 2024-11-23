package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaConsolaGrafica extends JFrame implements IVista, Observador {
    private ControladorJuegoHanabi controlador;
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
            case INICIAR_PARTIDA -> intentarIniciarJuego();
            case MENU_ACCIONES_JUGADOR -> mostrarMenuAccionesJugador();
            default -> mostrarMensaje("Comando no reconocido.");
        }
    }

    // Menús y comandos para cada estado
    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        mostrarMensaje("""
            Menú Principal:
            1. Registrar Jugador
            2. Iniciar Partida
            3. Salir
            Seleccione una opción:
            """);
    }

    private void procesarComandoMenuPrincipal(String comando) {
        switch (comando) {
            case "1" -> iniciarRegistroJugador();
            case "2" -> intentarIniciarJuego();
            case "3" -> salirJuego();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2' o '3'.");
        }
    }

    private void iniciarRegistroJugador() {
        if (jugadoresRegistrados.size() >= 4) {
            mostrarMensaje("Ya se ha alcanzado el límite máximo de jugadores.");
            return;
        }
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
            mostrarMenuPrincipal();
            return;
        }

        Jugador nuevoJugador = controlador.crearJugador(nombre);
        if (nuevoJugador != null) {
            jugadoresRegistrados.add(nuevoJugador);
            mostrarMensaje("Jugador '" + nombre + "' registrado.");
        } else {
            mostrarMensaje("Error al registrar el jugador. Intente nuevamente.");
        }

        if (jugadoresRegistrados.size() == 4) {
            mostrarMensaje("Se ha alcanzado el límite máximo de jugadores. Puede iniciar la partida.");
            mostrarMenuPrincipal();
        } else {
            mostrarMensaje("Jugadores registrados: " + jugadoresRegistrados.size() + ". Faltan " +
                    (2 - jugadoresRegistrados.size()) + " jugadores más para iniciar.");
        }
    }

    private void intentarIniciarJuego() {
        if (jugadoresRegistrados.size() < 2) {
            mostrarMensaje("Se necesitan al menos 2 jugadores para iniciar el juego.");
            return;
        }

        controlador.iniciarJuego(jugadoresRegistrados);
        controlador.iniciarTurno();
        mostrarMensaje("El juego ha comenzado. ¡Que comience la partida!");
        estado = EstadoVistaConsola.JUEGO_INICIADO;

        crearVistasPorJugador();
    }

    // Crea vistas independientes para cada jugador y muestra sus manos
    private void crearVistasPorJugador() {
        for (Jugador jugador : jugadoresRegistrados) {
            VistaConsolaGrafica vistaJugador = new VistaConsolaGrafica(controlador, jugador);
            controlador.agregarObservador(vistaJugador);
        }
    }

    // Muestra el menú de acciones para el jugador actual
    private void mostrarMenuAccionesJugador() {
        mostrarMensaje("""
            Menú de Acciones:
            1. Jugar Carta
            2. Descartar Carta
            3. Dar Pista
            Seleccione una opción:
            """);
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
        mostrarMensaje("Es el turno dea: " + jugador.getNombre());
    }

    @Override
    public void mostrarManoJugador(Jugador jugador) {
        // Implementación futura
    }

    @Override
    public void mostrarInicioJuego() {
        // Implementación futura
    }
    @Override
    public void actualizar(Eventos evento, Object data) {

    }



    @Override
    public void notificar(Eventos evento) {
        switch (evento) {
            case JUEGO_INICIADO:
                mostrarMensaje("¡El juego ha comenzado de verdad!");
                break;
            case INICIO_TURNO:
                mostrarMensaje("¡Es el turno de " + controlador.obtenerJugadorActual().getNombre() + "!");
                break;
            case CAMBIO_TURNO:
                mostrarMensaje("¡El turno ha cambiado!");
                break;
            case VICTORIA:
                mostrarMensaje("¡El juego ha terminado con una victoria!");
                break;
            case DERROTA:
                mostrarMensaje("¡El juego ha terminado con derrota!");
                break;
            case FICHA_PISTA_USADA:
                mostrarMensaje("Se ha utilizado una ficha de pista.");
                break;
            case FALTA_FICHA_PISTA:
                mostrarMensaje("No hay fichas de pista disponibles.");
                break;
            case FICHA_VIDA_PERDIDA:
                mostrarMensaje("Se ha perdido una ficha de vida.");
                break;
            case FALTA_FICHA_VIDA:
                mostrarMensaje("No hay fichas de vida disponibles.");
                break;
            case FALTA_FICHA_PISTA_USADA:
                mostrarMensaje("No hay fichas de pista usadas para recuperar.");
                break;
            case NO_ES_TURNO:
                mostrarMensaje("No es tu turno.");
                break;
            case NO_HAY_PISTA_DIPONIBLE:
                mostrarMensaje("No hay fichas de pista disponibles para dar.");
                break;
            case NO_HAY_PISTAS_USADAS:
                mostrarMensaje("No se han usado pistas que puedas descartar.");
                break;
            /*case ERROR_REGISTRO_JUGADOR:
                mostrarMensaje("Error en el registro del jugador: " + evento.getMessage());
                break;
            case ERROR_CREACION_JUGADOR:
                mostrarMensaje("Error en la creación del jugador: " + evento.getMessage());
                break;*/
            default:
                mostrarMensaje("Evento desconocido.");
                break;
        }
    }

    @Override
    public void notificar(Eventos evento, Object datoAenviar) {
        switch (evento) {
            case JUGADOR_JUGO_CARTA:
                mostrarMensaje("¡El jugador ha jugado una carta!");
                break;
            case JUGADOR_DESCARTO_CARTA:
                mostrarMensaje("¡El jugador ha descartado una carta!");
                break;
            case JUGADOR_TOMO_CARTA:
                mostrarMensaje("¡El jugador ha tomado una carta!");
                break;
            case PISTA_CREADA:
                // Aquí recibimos la pista creada, puedes mostrar información sobre ella
                Pista pista = (Pista) datoAenviar;
                mostrarMensaje("Pista creada: " + pista.toString());
                break;
            case OBTENER_NOMBRE_JUGADOR:
                // Aquí recibimos el nombre del jugador
                String nombre = (String) datoAenviar;
                mostrarMensaje("El nombre del jugador es: " + nombre);
                break;
            case OBTENER_CANTIDAD_JUGADORES:
                // Aquí recibimos la cantidad de jugadores
                int cantidadJugadores = (int) datoAenviar;
                mostrarMensaje("La cantidad de jugadores es: " + cantidadJugadores);
                break;
            /*case OBTENER_JUGADORES:
                // Aquí recibimos la lista de jugadores
                Jugador[] jugadores = (Jugador[]) datoAenviar;
                mostrarJugadores(jugadores);
                break;
            case MOSTRAR_MANO_JUGADOR_PISTA:
                // Aquí recibimos el jugador cuyo mano debemos mostrar para dar pista
                Jugador jugadorPista = (Jugador) datoAenviar;
                mostrarManoJugadorParaPista(jugadorPista);
                break;*/
            case MOSTRAR_VISTA_JUGADOR:
                System.out.println("llego bien");
                // Verifica que representacionCartas sea un Map
                if (datoAenviar instanceof Map<?, ?>) {
                    // Realiza el cast seguro
                    Map<Jugador, List<CartaRepresentacion>> vistaJugadores =
                            (Map<Jugador, List<CartaRepresentacion>>) datoAenviar;

                    // Procesa y muestra los datos
                    StringBuilder mensaje = new StringBuilder();
                    for (Map.Entry<Jugador, List<CartaRepresentacion>> entry : vistaJugadores.entrySet()) {
                        Jugador jugador = entry.getKey();
                        List<CartaRepresentacion> mano = entry.getValue();

                        mensaje.append("Mano de ").append(jugador.getNombre()).append(":\n");
                        for (CartaRepresentacion carta : mano) {
                            if (carta.isVisible()) {
                                mensaje.append("  - ").append(carta.getColor()).append(" ").append(carta.getValor()).append("\n");
                            } else {
                                mensaje.append("  - [CARTA OCULTA]\n");
                            }
                        }
                    }

                    // Usa mostrarMensaje para mostrar toda la información
                    mostrarMensaje(mensaje.toString());
                } else {
                    mostrarMensaje("El objeto recibido no es del tipo esperado.");
                }
                break;

            case MOSTRAR_MANO:
                // Aquí recibimos la mano del jugador
                String mano = (String) datoAenviar;
                mostrarMensaje("La mano del jugador es: " + mano);
                break;
            case MOSTRAR_MANO_NUMEROS:
                // Aquí recibimos la mano del jugador con números
                String manoNumeros = (String) datoAenviar;
                mostrarMensaje("La mano de números del jugador es: " + manoNumeros);
                break;
            case MOSTRAR_MANO_COLORES:
                // Aquí recibimos la mano del jugador con colores
                String manoColores = (String) datoAenviar;
                mostrarMensaje("La mano de colores del jugador es: " + manoColores);
                break;
            default:
                mostrarMensaje("Evento desconocido o no manejado con datos.");
                break;
        }
    }




}




