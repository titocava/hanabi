package ar.edu.unlu.Hanabi.Vista;
import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


public class VistaConsolaGraficaJugador extends JFrame implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final Jugador jugador;
    private SeleccionCarta seleccionCarta;

    public VistaConsolaGraficaJugador(ControladorJuegoHanabi controlador, Jugador jugador) {
        this.controlador = controlador;
        this.jugador = jugador;
        this.txtSalida = new JTextArea();
        this.txtEntrada = new JTextField();
        this.btnEnter = new JButton("Enter");
        this.seleccionCarta = new SeleccionCarta(txtSalida, txtEntrada, btnEnter);

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
        // Configura el evento de clic en el botón
        btnEnter.addActionListener((ActionEvent e) -> procesarEntrada(txtEntrada.getText().trim()));

        // Configura el evento de presionar la tecla Enter en el campo de texto
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnEnter.doClick();  // Simula el clic en el botón "Enter" cuando se presiona Enter
                    e.consume();  // Evita que se añadan saltos de línea al presionar Enter
                }
            }
        });
    }




    private void procesarEntrada(String entrada) {
        txtEntrada.setText(""); // Limpia el campo de texto
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.");
            return;
        }

        // Procesar comandos según el estado actual
        switch (estado) {
            case MENU_ACCIONES_JUGADOR -> procesarComandoAccionesJugador(entrada);
            default -> mostrarMensaje("Comando no reconocido.");
        }

        // Asegura que el foco vuelva al campo de entrada
        txtEntrada.requestFocusInWindow();
    }


    private void procesarComandoAccionesJugador(String comando) {
        switch (comando) {
            case "1" -> jugarCarta();
            case "2" -> darPista();
            case "3" -> descartarCarta();
            case "4" -> mostrarManoDeJugador();
            case "5" -> mostrarMenuDeAccion();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2', '3', '4' o '5'.");
        }
    }

    private void jugarCarta() {
        List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(jugador);

        // Reiniciar el selector antes de usarlo
        reiniciarSelector();

        seleccionCarta.seleccionar(manoJugador, cartaSeleccionada -> {
            System.out.println("El usuario seleccionó: " + cartaSeleccionada);
            controlador.jugadorJuegaCarta(jugador, cartaSeleccionada);
        });
    }

    private void darPista() {

        SeleccionJugador seleccionJugador = new SeleccionJugador(txtSalida, txtEntrada, btnEnter);
        List<Jugador> listaDeJugadores = controlador.obtenerListaJugadores();
         seleccionJugador.seleccionar(listaDeJugadores, jugadorSeleccionado -> {
            System.out.println("El usuario seleccionó: " + jugadorSeleccionado.getNombre());
            mostrarMensaje("Iniciando flujo para dar una pista.");
            GeneradorDePista generadorDePista = new GeneradorDePista(txtSalida, txtEntrada, btnEnter, controlador);
            generadorDePista.generar(jugadorSeleccionado, pista -> {
                System.out.println("Pista generada: " + pista);
                controlador.jugadorDaPista(jugadorSeleccionado, pista);
            });
        });

    }

    private void descartarCarta() {
        if (controlador.obtenerFichasDePistaUsadas() >= 1) {
            mostrarMensaje("Selecciona una carta para descartar.");
            SeleccionCarta seleccionCarta = new SeleccionCarta(txtSalida, txtEntrada, btnEnter);
            List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(jugador);
            seleccionCarta.seleccionar(manoJugador, cartaSeleccionada -> {
                System.out.println("El usuario seleccionó: " + cartaSeleccionada);
                controlador.jugadorDescartaCarta(jugador, cartaSeleccionada);
            });

        } else {
            mostrarMensaje("Aún no tiene fichas de pistas usadas para descartar.");
            mostrarMenuDeAccion();

        }
    }

    public void mostrarInformacionDeJugadoresInicial() {
        procesarMostrarCartasDeMano(controlador.obtenerManoJugadorNoVisible(jugador));
        procesarMostrarManos(controlador.retornarManosVisiblesJugadores(jugador, controlador.obtenerListaJugadores()));
        procesarDatosTablero(controlador.obtenerDatosTablero());

    }

     public void mostrarManoDeJugador(){
        procesarMostrarCartasDeMano(controlador.obtenerManoJugadorNoVisible(jugador));
        procesarMostrarManos(controlador.retornarManosVisiblesJugadores(jugador, controlador.obtenerListaJugadores()));

    }


    public void procesarDatosTablero(List<Object> datos) {
        mostrarMensaje("Cartas restantes en el mazo: " + datos.get(0));
        mostrarMensaje("Fichas de vida: " + datos.get(1));
        mostrarMensaje("Fichas de pista usadas: " + datos.get(2));
        mostrarMensaje("Fichas de pista: " + datos.get(3));

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
            case INICIAR_JUEGO:
                reiniciarSelector();
                mostrarInformacionDeJugadoresInicial();
                mostrarMenuDeAccion();
                break;

            case JUGADOR_JUGO_CARTA, CAMBIO_TURNO:
                reiniciarSelector();
                mostrarInformacionDeJugadoresInicial();
                mostrarMenuDeAccion();
                break;

            case NO_HAY_PISTAS_USADAS:

                mostrarMensaje("Aún no tiene fichas de pistas usadas para descartar.");
                break;

            case PISTA_DADA:
                reiniciarSelector();
                mostrarMensaje("¡Pista dada!");
                mostrarInformacionDeJugadoresInicial();
                mostrarMenuDeAccion();
                break;

            case JUGADOR_DESCARTO_CARTA:
                reiniciarSelector();
                mostrarMensaje("¡Has descartado una carta!");
                mostrarInformacionDeJugadoresInicial();
                mostrarMenuDeAccion();
                break;

            default:
                mostrarMensaje("Evento no manejado: " + evento);
                break;
        }
    }


    @Override
    public void notificar(Eventos evento, Object datoAenviar) {
        /*switch (evento) {
            case MOSTRAR_MANO -> {
                mostrarMensaje("Mano del jugador:");
                if (datoAenviar instanceof List<?> cartas && !cartas.isEmpty() && cartas.get(0) instanceof Carta) {
                    procesarMostrarCartasDeMano((List<Carta>) cartas); // Casteo seguro
                } else {
                    mostrarMensaje("No hay cartas en la mano.");
                }
            }

            case MOSTRAR_MANOS_VISIBLES_RESTO -> {
                mostrarMensaje("Manos visibles del resto de jugadores:");
                if (datoAenviar instanceof List<?> manos && !manos.isEmpty() && manos.get(0) instanceof Map) {
                    // Asegúrate de que es el tipo correcto
                    List<Map<Jugador, List<Carta>>> manosList = (List<Map<Jugador, List<Carta>>>) manos;
                    procesarMostrarManos(manosList); // Llamada con cast validado
                } else {
                    mostrarMensaje("No hay manos visibles.");
                }
            }

            case ACTUALIZAR_TABLERO -> {
                mostrarMensaje("Información actualizada del tablero:");
                if (datoAenviar instanceof List<?> datos && !datos.isEmpty()) {
                    procesarDatosTablero((List<Object>) datos); // Casteo
                } else {
                    mostrarMensaje("Error al actualizar el tablero: datos inválidos o vacíos.");
                }
            }

            default -> mostrarMensaje("Evento no manejado: " + evento);
        }*/
    }



    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarFinJuego() {
        mostrarMensaje("¡El juego ha terminado!");
    }

    public void procesarMostrarManos(List<Map<Jugador, List<Carta>>> listaManos) {
        if (listaManos.isEmpty()) {
            mostrarMensaje("No hay manos visibles para mostrar.");
            return;
        }

        for (Map<Jugador, List<Carta>> manos : listaManos) {

            for (Map.Entry<Jugador, List<Carta>> entry : manos.entrySet()) {
                Jugador jugador = entry.getKey();
                List<Carta> mano = entry.getValue();

                mostrarMensaje("Mano de " + jugador.getNombre() + ":");

                if (mano.isEmpty()) {
                    mostrarMensaje("  Sin cartas.");
                } else {
                    for (Carta carta : mano) {
                        mostrarMensaje("  " + carta.toString());
                    }
                }

                mostrarMensaje("====================");
            }
        }
    }



    public void procesarMostrarCartasDeMano(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            mostrarMensaje("La mano está vacía.");
            return;
        }

        mostrarMensaje("Cartas visibles:");
        for (int i = 0; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);

            // Verificar si la carta está revelada y mostrar el mensaje en consecuencia
            if (carta.esRevelada()) {
                mostrarMensaje((i + 1) + ". " + carta.toString());  // Si la carta está revelada, la mostramos normalmente
            } else {
                mostrarMensaje((i + 1) + ". Carta no visible");  // Si no está revelada, indicamos que no es visible
            }
        }
        mostrarMensaje("--------------------");
    }

    private void configurarListenerDinamico(ActionListener listener) {
        // Elimina los listeners anteriores del botón
        for (ActionListener al : btnEnter.getActionListeners()) {
            btnEnter.removeActionListener(al);
        }
        btnEnter.addActionListener(listener);

        // Configura el evento de presionar la tecla Enter en el campo de texto
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnEnter.doClick(); // Simula el clic en el botón
                    e.consume(); // Evita eventos adicionales o saltos de línea
                }
            }
        });

        // Asegura que el foco esté en el campo de texto
        txtEntrada.requestFocusInWindow();
    }

    // Método para reiniciar el selector
    private void reiniciarSelector() {
        // Eliminar listeners anteriores
        for (ActionListener al : btnEnter.getActionListeners()) {
            btnEnter.removeActionListener(al);
        }

        // Volver a configurar los eventos base
        configurarEventos();
    }









}

