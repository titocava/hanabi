package ar.edu.unlu.Hanabi.Vista;
import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class VistaConsolaGraficaJugador extends JFrame implements IVista, Observador {
    private ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final Jugador jugador;  // Esta vista es para un jugador específico
    private Eventos eventos;

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
        mostrarMenuAccionesJugador();
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

    // Método para procesar los comandos del jugador
    private void procesarComandoAccionesJugador(String comando) {
        switch (comando) {
            case "1" -> jugarCarta();
            case "2" -> darPista();
            case "3" -> descartarCarta();
            case "4" -> mostrarManoDeJugadores();
            case "5" -> mostrarMenuPrincipal();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2', '3', '4' o '5'.");
        }
    }

    // Acciones que el jugador puede tomar en su turno
    private void jugarCarta() {

        mostrarMensaje("Carta jugada.");
    }

    private void darPista() {

        mostrarMensaje("Pista dada.");
    }

    private void descartarCarta() {

        mostrarMensaje("Carta descartada.");
    }

    private void mostrarManoDeJugadores(Object datoAenviar) {
        // Verifica que el objeto dado sea un Map
        if (datoAenviar instanceof Map<?, ?>) {
            // Realiza el cast seguro a Map<Jugador, List<CartaRepresentacion>>
            Map<Jugador, List<CartaRepresentacion>> vistaJugadores =
                    (Map<Jugador, List<CartaRepresentacion>>) datoAenviar;

            // Procesa y muestra los datos
            StringBuilder mensaje = new StringBuilder();

            // Itera sobre cada entrada en el mapa de jugadores y sus manos
            for (Map.Entry<Jugador, List<CartaRepresentacion>> entry : vistaJugadores.entrySet()) {
                Jugador jugador = entry.getKey();
                List<CartaRepresentacion> mano = entry.getValue();

                // Agrega el nombre del jugador al mensaje
                mensaje.append("Mano de ").append(jugador.getNombre()).append(":\n");

                // Itera sobre las cartas de la mano del jugador
                for (CartaRepresentacion carta : mano) {
                    // Verifica si la carta está visible
                    if (carta.isVisible()) {
                        mensaje.append("  - ").append(carta.getColor()).append(" ").append(carta.getValor()).append("\n");
                    } else {
                        // Si la carta no es visible, muestra un mensaje indicando que está oculta
                        mensaje.append("  - [CARTA OCULTA]\n");
                    }
                }
            }

            // Muestra el mensaje con la información procesada
            mostrarMensaje(mensaje.toString());
        } else {
            // Si el objeto no es un Map<Jugador, List<CartaRepresentacion>>, muestra un mensaje de error
            mostrarMensaje("Error: Datos incorrectos para mostrar las manos de los jugadores.");
        }
    }


    private void mostrarMenuPrincipal() {
        // Regresa al menú principal si es necesario
        mostrarMensaje("Menú principal:\n1. Jugar carta\n2. Dar pista\n3. Descartar carta\n4. Ver tu mano\n5. Salir");
        estado = EstadoVistaConsola.MENU_ACCIONES_JUGADOR;
    }

    @Override
    public void notificar(Eventos evento) {
        switch (evento) {
            case JUGADA_REALIZADA:
                // El controlador o el modelo enviarán este evento después de que el jugador juegue una carta.
                mostrarMensaje("¡Has jugado una carta!");
                break;
            case PISTA_DADA:
                // El controlador o el modelo enviarán este evento cuando se dé una pista.
                mostrarMensaje("¡Pista dada!");
                break;
            case CARTA_DESCARTADA:
                // El controlador o el modelo enviarán este evento cuando se descarte una carta.
                mostrarMensaje("¡Has descartado una carta!");
                break;
            case ACTUALIZAR_MANO:
                // Actualiza la mano del jugador.

                break;
            case FIN_DE_TURNO:
                // El turno ha terminado, o la acción del jugador ha sido procesada.
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
            case JUGADA_REALIZADA:
                mostrarMensaje("¡Has realizado una jugada!");
                break;

            case PISTA_DADA:
                mostrarMensaje("Has dado una pista a otro jugador.");
                break;

            case CARTA_DESCARTADA:
                mostrarMensaje("Has descartado una carta.");
                break;

            case ACTUALIZAR_MANO:
                mostrarManoDeJugadores();
                break;

            case FIN_DE_TURNO:
                mostrarMensaje("Tu turno ha terminado.");
                break;

            case JUEGO_INICIADO:
                mostrarMensaje("¡El juego ha comenzado!");
                break;

            case JUEGO_TERMINADO:
                mostrarMensaje("El juego ha terminado.");
                break;

            case NOTIFICACION_GENERAL:
                mostrarMensaje("Notificación general recibida.");
                break;

            default:
                mostrarMensaje("Evento desconocido.");
                break;
        }


    }
    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());

    }}

