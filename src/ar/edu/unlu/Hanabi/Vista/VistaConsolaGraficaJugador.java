package ar.edu.unlu.Hanabi.Vista;
import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.List;


public class VistaConsolaGraficaJugador extends JFrame implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final Jugador jugador;  // Esta vista es para un jugador específico

    public VistaConsolaGraficaJugador(ControladorJuegoHanabi controlador, Jugador jugador) {
        this.controlador = controlador;
        this.jugador = jugador;
        this.txtSalida = new JTextArea();
        this.txtEntrada = new JTextField();
        this.btnEnter = new JButton("Enter");

        // Configuración básica de la vista
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtSalida);
        add(scrollPane, BorderLayout.CENTER);
        configurarVentana();
        configurarComponentes();
        configurarEventos();

    }

    private void configurarVentana() {
        setTitle("Hanabi - Juego en Consola - " + jugador.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void configurarComponentes() {
        JScrollPane scrollPane = new JScrollPane(txtSalida);
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(txtEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnter, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnEnter.addActionListener((ActionEvent e) -> procesarEntrada(txtEntrada.getText().trim()));
    }

    // Método para procesar la entrada del jugador
    private void procesarEntrada(String entrada) {
        txtEntrada.setText(""); // Limpiar el campo de entrada
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.");
            return;
        }

        switch (estado) {
            case MENU_ACCIONES_JUGADOR -> procesarComandoAccionesJugador(entrada);
            default -> mostrarMensaje("Comando no reconocido.");
        }
    }

    private void procesarComandoAccionesJugador(String comando) {
        switch (comando) {
            case "1" -> jugarCarta();
            case "2" -> darPista();
            case "3" -> descartarCarta();
            case "4" -> mostrarManoDeJugadores();
            case "5" -> mostrarMenuDeAccion();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2', '3', '4' o '5'.");
        }
    }

    // Acciones que el jugador puede tomar en su turno
    private void jugarCarta() {
        mostrarMensaje("Carta jugada.");
        seleccionarCarta(controlador.obtenerJugadorActual().getMano());


    }

    private void darPista() {
        mostrarMensaje("Pista dada.");
    }

    private void descartarCarta() {
        mostrarMensaje("Carta descartada.");
    }

    // Mostrar las manos de los jugadores, con sus cartas
    public void mostrarInformacionDeJugadoresInicio() {
        procesarMostrarManoJugador(controlador.manoJugadorInicio(jugador));
        ProcesarMostrarManosVisiblesRestantes(controlador.manosRestoJugadoresInicio(jugador));  // Llamamos al controlador para obtener la mano
        procesarDatosTablero(controlador.obtenerDatosTablero());
        mostrarMenuDeAccion();
    }

    public void mostrarManoDeJugadores(){
        controlador.obtenerManoJugadorInstanciado(jugador);
        controlador.manejarObtenerManosVisiblesResto(jugador);

    }


    //metodos para iniciar la partida
    public void procesarMostrarManoJugador(List<String> manoJugador) {
        mostrarMensaje("Mano del jugador:");
        if (manoJugador.isEmpty()) {
            mostrarMensaje("No hay cartas en la mano.");
        } else {
            for (String carta : manoJugador) {
                mostrarMensaje(carta);  // Mostrar cada carta una vez
            }
        }
    }
    //metodos para iniciar la partida
    public void ProcesarMostrarManosVisiblesRestantes(List<Map<String, List<String>>> listaManosVisibles) {
        mostrarMensaje("Manos visibles del resto de jugadores:");
        if (listaManosVisibles.isEmpty()) {
            mostrarMensaje("No hay manos visibles.");
        } else {
            for (Map<String, List<String>> manos : listaManosVisibles) {
                for (Map.Entry<String, List<String>> entry : manos.entrySet()) {
                    mostrarMensaje("Cartas de " + entry.getKey() + ": " + entry.getValue());
                }
            }
        }
    }

    public void procesarDatosTablero(List<Object> datos) {
        // Mostrar las cartas restantes en el mazo
        mostrarMensaje("Cartas restantes en el mazo: " + datos.get(0));

        // Mostrar las fichas de vida
        mostrarMensaje("Fichas de vida: " + datos.get(1));

        // Mostrar las fichas de pista usadas
        mostrarMensaje("Fichas de pista usadas: " + datos.get(2));

        // Mostrar las fichas de pista
        mostrarMensaje("Fichas de pista: " + datos.get(3));

        // Mostrar la información de los castillos
        List<CastilloDeCartas> castillos = (List<CastilloDeCartas>) datos.get(4);
        if (castillos.isEmpty()) {
            mostrarMensaje("No hay castillos en el tablero.");
        } else {
            for (CastilloDeCartas castillo : castillos) {
                mostrarMensaje("Castillo para color " + castillo.getColor() + ": " + castillo.getCartas());
            }
        }
    }









    private void mostrarMenuDeAccion() {
        Jugador jugadorActual = controlador.obtenerJugadorActual();

        // Comprobar si el jugador actual es el jugador cuya vista está activa
        if (jugadorActual.equals(this.jugador)) {
        mostrarMensaje("Menú principal:\n1. Jugar carta\n2. Dar pista\n3. Descartar carta\n4. Ver tu mano\n5. Salir");
        estado = EstadoVistaConsola.MENU_ACCIONES_JUGADOR;
        }else{
            mostrarMensaje("Espera tu turno.");
        }
    }

    @Override
    public void notificar(Eventos evento) {
        switch (evento) {
            case JUGADA_REALIZADA:
                mostrarMensaje("¡Has jugado una carta!");
                break;
            case PISTA_DADA:
                mostrarMensaje("¡Pista dada!");
                break;
            case CARTA_DESCARTADA:
                mostrarMensaje("¡Has descartado una carta!");
                break;
            case ACTUALIZAR_MANO:
                mostrarMensaje("Mano actualizada.");
                break;
            case FIN_DE_TURNO:
                mostrarMensaje("Tu turno ha terminado.");
                break;
            default:
                mostrarMensaje("Evento no reconocido.");
                break;
        }
    }

    @Override
    public void notificar(Eventos evento, Object datoAenviar) {
        switch (evento) {
            case MOSTRAR_MANO:
                mostrarMensaje("Mano del jugador:");
                List<String> manoJugador = (List<String>) datoAenviar;
                if (manoJugador.isEmpty()) {
                    mostrarMensaje("No hay cartas en la mano.");
                } else {
                    for (String carta : manoJugador) {
                        mostrarMensaje(carta);  // Mostramos cada carta una vez
                    }
                }
                break;

            case MOSTRAR_MANOS_VISIBLES_RESTO:
                mostrarMensaje("Manos visibles del resto de jugadores:");
                List<Map<String, List<String>>> listaManosVisibles = (List<Map<String, List<String>>>) datoAenviar;
                if (listaManosVisibles.isEmpty()) {
                    mostrarMensaje("No hay manos visibles.");
                } else {
                    for (Map<String, List<String>> manos : listaManosVisibles) {
                        for (Map.Entry<String, List<String>> entry : manos.entrySet()) {
                            // Mostrar solo si no ha sido mostrado previamente
                            mostrarMensaje("Cartas de " + entry.getKey() + ": " + entry.getValue());
                        }
                    }
                }
                break;
            case JUGADOR_TOMO_CARTA:
                mostrarInformacionDeJugadoresInicio();
                controlador.cambiarTurno();
                break;


            default:
                mostrarMensaje("Evento no reconocido.");
                break;
        }
    }


    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public void iniciar() {

        setVisible(true); // Asegura que la vista sea visible cuando se inicie
        mostrarInformacionDeJugadoresInicio();
    }

    @Override
    public void mostrarFinJuego() {
        mostrarMensaje("¡El juego ha terminado!");
    }

    private void seleccionarCarta(List<Carta> mano) {
        // Muestra las cartas visibles con índices
        StringBuilder mensaje = new StringBuilder("Seleccione una carta de su mano:\n");
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            mensaje.append(i + 1).append(". ").append(carta.toString()).append("\n"); // toString personalizado para mostrar carta
        }
        mostrarMensaje(mensaje.toString()); // Mostrar en JTextArea

        // Cambia el evento del botón para procesar la entrada
        btnEnter.addActionListener((ActionEvent e) -> {
            try {
                int seleccion = Integer.parseInt(txtEntrada.getText().trim()) - 1; // Índices comienzan en 1
                txtEntrada.setText(""); // Limpia la entrada
                if (seleccion >= 0 && seleccion < mano.size()) {
                    Carta cartaSeleccionada = mano.get(seleccion);
                    mostrarMensaje("Has seleccionado: " + cartaSeleccionada.toString());
                    // Aquí llamas al método del controlador para arrojar o jugar la carta
                    procesarAccionCarta(cartaSeleccionada);
                } else {
                    mostrarMensaje("Selección inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Entrada no válida. Por favor ingrese un número.");
            }
        });
    }

    // Ejemplo de procesamiento de la acción con la carta seleccionada
    private void procesarAccionCarta(Carta cartaSeleccionada) {
        // Aquí decides qué hacer con la carta seleccionada (descartar, jugar, etc.)
        mostrarMensaje("Procesando acción para la carta: " + cartaSeleccionada.toString());
        controlador.jugadorJuegaCarta(jugador, cartaSeleccionada);
    }





}

