package ar.edu.unlu.Hanabi.Vista;

public enum EstadoVistaConsola {
    MENU_PRINCIPAL,               // Estado cuando se muestra el menú principal (Nueva partida, Guardar partida, Salir)
    NUEVA_PARTIDA,

    SELECCIONAR_JUGADORES,        // Estado cuando se selecciona la cantidad de jugadores
    JUGADOR_TURNO,             // Estado cuando es el turno de un jugador para hacer una acción
    JUGAR_CARTA,                  // Estado para jugar una carta (interacción directa con las cartas del jugador)
    DESCARTAR_CARTA,              // Estado para descartar una carta (si se tienen fichas de pista usadas)
    DAR_PISTA,                    // Estado para dar una pista (si se tienen fichas de pista)
    JUGADOR_ESPERA,            // Estado cuando los jugadores en espera ven que es el turno de otro jugador
    JUEGO_EN_CURSO,
    REGISTRAR_JUGADOR,
    INICIAR_PARTIDA,
    REGISTRAR_OTRO_JUGADOR,
    MENU_ACCIONES_JUGADOR,
}