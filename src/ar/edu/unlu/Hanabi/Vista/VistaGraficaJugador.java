package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class VistaGraficaJugador extends JPanel implements IVista, Observador {
    private final ControladorJuegoHanabi controlador;
    private final Jugador jugador;

    private final JPanel panelCentral; // Donde se muestran las cartas o información del jugador
    private final JLabel lblTitulo; // Para mostrar el título o estado del turno
    private final JTextArea txtSalida; // Texto de salida
    private final JTextField txtEntrada; // Entrada de texto
    private final JButton btnEnter; // Botón de acción
    private final JButton btnJugarCarta, btnDarPista, btnDescartarCarta, btnVerMano, btnSalir;

    public VistaGraficaJugador(ControladorJuegoHanabi controlador, Jugador jugador) {
        this.controlador = controlador;
        this.jugador = jugador;

        // Configuración de la vista
        setLayout(new BorderLayout());

        // Crear componentes
        lblTitulo = new JLabel("Bienvenido, " + jugador.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(0, 1)); // Mostrar elementos en columnas
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtSalida = new JTextArea();
        txtEntrada = new JTextField();
        btnEnter = new JButton("Confirmar");
        btnJugarCarta = new JButton("Jugar Carta");
        btnDarPista = new JButton("Dar Pista");
        btnDescartarCarta = new JButton("Descartar Carta");
        btnVerMano = new JButton("Ver tu Mano");
        btnSalir = new JButton("Salir");

        configurarEventos();
        agregarComponentes();
    }

    private void agregarComponentes() {
        // Parte superior con título
        add(lblTitulo, BorderLayout.NORTH);

        // Parte central
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnJugarCarta);
        panelInferior.add(btnDarPista);
        panelInferior.add(btnDescartarCarta);
        panelInferior.add(btnVerMano);
        panelInferior.add(btnSalir);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnJugarCarta.addActionListener(e -> jugarCarta());
        btnDarPista.addActionListener(e -> {
            try {
                darPista();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDescartarCarta.addActionListener(e -> descartarCarta());
        btnVerMano.addActionListener(e -> mostrarManoDeJugador());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void jugarCarta() {
        // Crear los componentes necesarios
        SeleccionCartaGrafica seleccionCarta = new SeleccionCartaGrafica(txtSalida, txtEntrada, btnEnter);
        List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(jugador);
        seleccionCarta.seleccionar(manoJugador, cartaSeleccionada -> {
            controlador.jugadorJuegaCarta(jugador, cartaSeleccionada);
            actualizarVista("Has jugado la carta: " + cartaSeleccionada);
        });

        // Agregar el JPanel de la vista al panel central
        panelCentral.removeAll();
        panelCentral.add(seleccionCarta);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void darPista() throws RemoteException {
        // Crear los componentes necesarios
        SeleccionJugadorGrafico seleccionJugador = new SeleccionJugadorGrafico(txtSalida, txtEntrada, btnEnter);
        List<Jugador> listaDeJugadores = controlador.obtenerListaJugadores();
        seleccionJugador.seleccionar(listaDeJugadores, jugadorSeleccionado -> {
            GeneradorDePistaGrafico generadorDePista = new GeneradorDePistaGrafico(txtSalida, txtEntrada, btnEnter, controlador);
            generadorDePista.generar(jugadorSeleccionado, pista -> {
                controlador.jugadorDaPista(jugadorSeleccionado, pista);
                actualizarVista("Has dado una pista al jugador: " + jugadorSeleccionado.getNombre());
            });
        });

        // Agregar el JPanel de la vista al panel central
        panelCentral.removeAll();
        panelCentral.add(seleccionJugador);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void descartarCarta() {
        // Crear un panel para la selección de cartas
        SeleccionCartaGrafica seleccionCarta = new SeleccionCartaGrafica(txtSalida, txtEntrada, btnEnter);
        List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(jugador);
        seleccionCarta.seleccionar(manoJugador, cartaSeleccionada -> {
            controlador.jugadorDescartaCarta(jugador, cartaSeleccionada);
            actualizarVista("Has descartado la carta: " + cartaSeleccionada);
        });

        // Agregar el JPanel de la vista al panel central
        panelCentral.removeAll();
        panelCentral.add(seleccionCarta);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void mostrarManoDeJugador() {
        List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(jugador);
        panelCentral.removeAll();
        if (manoJugador.isEmpty()) {
            actualizarVista("Tu mano está vacía.");
        } else {
            for (Carta carta : manoJugador) {
                JLabel cartaLabel = new JLabel(carta.toString());
                panelCentral.add(cartaLabel);
            }
        }
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void actualizarVista(String mensaje) {
        lblTitulo.setText(mensaje);
        panelCentral.removeAll();
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    @Override
    public void notificar(Eventos evento) {
        switch (evento) {
            case CAMBIO_TURNO:
                lblTitulo.setText("Es tu turno.");
                break;
            case JUGADOR_DESCARTO_CARTA:
                lblTitulo.setText("Carta descartada.");
                break;
            case PISTA_DADA:
                lblTitulo.setText("Pista dada.");
                break;
            default:
                lblTitulo.setText("Evento: " + evento);
        }
    }

    @Override
    public void notificar(Eventos evento, Object datoAenviar) {
        if (evento == Eventos.MOSTRAR_MANO && datoAenviar instanceof List<?> cartas) {
            mostrarManoDeJugador();
        }
    }

    @Override
    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        lblTitulo.setText(mensaje);
    }

    @Override
    public void mostrarFinJuego() {
        mostrarMensaje("¡El juego ha terminado!");
    }
}
