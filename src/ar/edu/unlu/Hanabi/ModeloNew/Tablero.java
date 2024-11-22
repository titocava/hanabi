package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tablero {
    private static final int CARTAS_MAXIMAS_EN_MANO = 5;
    private final Mazo mazo;                     // Instancia del mazo
    private final Fichas fichas;                 // Instancia de las fichas de vida y pistas
    private List<CastilloDeCartas> castillos;   // Instancia de los castillos de cartas
    private List<Jugador> jugadores;             // Lista de jugadores
    private TipoPista pista;

    // Constructor
    public Tablero(List<Jugador> jugadores) {
        // Inicializa las instancias necesarias
        this.mazo = new Mazo();                  // Inicializa el mazo
        this.fichas = new Fichas();              // Inicializa las fichas
        this.castillos = new ArrayList<>();; // Inicializa los castillos de cartas
        this.jugadores = jugadores;             // Asigna la lista de jugadores

        // Crear los castillos para cada color de cartas
        for (ColorCarta color : ColorCarta.values()) {
            castillos.add(new CastilloDeCartas(color));
        }
    }


    // Método para iniciar el juego, repartir cartas y preparar el tablero
    public void iniciarJuego() {
        // Repartir cartas entre los jugadores al inicio del juego
        repartirCartas(jugadores);
    }

    // Método para repartir cartas a los jugadores al inicio del juego
    private void repartirCartas(List<Jugador> jugadores) {
        int cartasPorJugador = (jugadores.size() <= 3) ? 5 : 4; // 5 cartas para 2-3 jugadores, 4 para 4-5 jugadores
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < cartasPorJugador; i++) {
                Carta cartaRobada = mazo.robarCarta();
                if (cartaRobada != null) {
                    jugador.agregarCartaACartasEnMano(cartaRobada); // Añade carta a la mano del jugador
                }
            }
        }
    }


    public int getMazoActual() {
        return mazo.cartasRestantes();
    }

    public List<CastilloDeCartas> getCastillos() {
        return Collections.unmodifiableList(castillos);
    }

    public void darPista(Jugador jugadorDestino, Pista pista) {
        if (obtenerFichasDePista() <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }

        if (jugadorDestino == null || pista == null) {
            throw new IllegalArgumentException("El jugador destino y la pista no pueden ser nulos.");
        }

        for (Carta carta : jugadorDestino.getMano()) {
            if (carta.coincideConPista(pista)) {
                carta.revelar(); // Marca la carta como revelada
            }
        }
    }

    public void jugarCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalArgumentException("El jugador y la carta no pueden ser nulos.");
        }

        if (!jugador.getMano().contains(carta)) {
            throw new IllegalArgumentException("La carta no pertenece a la mano del jugador.");
        }

        // Remueve la carta de la mano del jugador
        jugador.getMano().remove(carta);

        // Procesa la carta jugada (puede ser añadida al mazo de fuegos artificiales, etc.)
        cartaJugada(carta);

        // El jugador roba una nueva carta si hay cartas disponibles
        if (!mazo.estaVacio()) {
            Carta nuevaCarta = mazo.robarCarta();
            jugador.agregarCartaACartasEnMano(nuevaCarta);
        } else {
            // Si no hay más cartas, el jugador no roba
            throw new IllegalArgumentException("El mazo está vacío. No se roban más cartas.");
        }
    }

    public void descartarCarta(Jugador jugador, Carta carta) {
        if (obtenerFichasDePistaUsadas() <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }
        // Verificamos si la carta está en la mano del jugador
        if (!jugador.getMano().contains(carta)) {
            throw new IllegalArgumentException("La carta no está en la mano del jugador.");
        }
        jugador.eliminarCartaDeLaMano(carta);

    }

    public void tomarCarta(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        if (jugador.getMano().size() >= CARTAS_MAXIMAS_EN_MANO) {
            throw new IllegalStateException("El jugador ya tiene el número máximo de cartas en la mano.");
        }

        if (mazo.estaVacio()) {
            throw new IllegalStateException("El mazo está vacío. No se pueden tomar más cartas.");
        }


        Carta cartaRobada = mazo.robarCarta();
        jugador.agregarCartaACartasEnMano(cartaRobada);
    }
    public boolean estaVacio() {
        return mazo.estaVacio(); // Retorna true si el mazo está vacío, false si no lo está
    }

    public void cartaJugada(Carta carta) {
        // Buscar el castillo correspondiente al color de la carta
        CastilloDeCartas castillo = null;
        for (CastilloDeCartas c : castillos) {
            if (c.getColor() == carta.getColor()) {
                castillo = c;
                break; // Se ha encontrado el castillo correspondiente
            }
        }

        // Si existe el castillo y se puede apilar la carta
        if (castillo != null) {
            boolean apilada = castillo.apilarCarta(carta);
            if (apilada) {
                System.out.println("Carta apilada correctamente en el castillo de " + carta.getColor());
                return;
            }
        }

        // Si no se pudo apilar, se desecha la carta y se pierde una vida
        desecharCarta(carta);
        reducirFichaVida();
    }


    private void desecharCarta(Carta carta) {
        System.out.println("Carta " + carta + " desechada.");
    }

    public int obtenerFichasDePista() {
        return fichas.getFichasDePista();
    }

    public int obtenerFichasDePistaUsadas() {
        return fichas.getFichasPistaUsadas();
    }


    public int obtenerFichasDeVida() {
        return fichas.getFichasDeVida();
    }


    public void reducirFichaPista() {
        fichas.usarFichaPista();
    }


    public void reducirFichaVida() {
        fichas.usarFichaVida();
    }


    public void recuperarFichaPista() {
        fichas.recuperarFichaPista();
    }




    public boolean todosLosCastillosCompletos() {
        for (CastilloDeCartas castillo : castillos) {
            if (!castillo.esCastilloCompleto()) {
                return false; // Si algún castillo no está completo, devuelve false
            }
        }
        return true; // Si todos los castillos están completos, devuelve true
    }

    public List<Jugador> obtenerJugadores() {
        return this.jugadores;  // Asumiendo que 'jugadores' es un campo en Tablero
    }



}
