package ar.edu.unlu.Hanabi.Vista;

public enum EstadoVistaConsola {

    // Estado relacionado con la vista principal del juego
    MENU_PRINCIPAL,
    JUEGO_INICIADO,
    JUEGO_TERMINADO,
    NUEVA_PARTIDA,
    INICIAR_PARTIDA,
    MENU_ACCIONES_JUGADOR,
    FIN_JUEGO,

    // Acciones
    JUGAR_CARTA,
    DESCARTAR_CARTA,
    DAR_PISTA,

    // Estados relacionados con el turno del jugador
    TURNO_JUGADOR_ACTUAL,
    TURNO_JUGADOR_EN_ESPERA,
    INICIO_TURNO,
    ACTUALIZAR_MANO,


    // Estados relacionados con las manos y las cartas
    MOSTRAR_MANO_JUGADOR_TURNO,
    MOSTRAR_MANOS_JUGADOR_ESPERA,
    MOSTRAR_MANO_PISTA_JUGADOR,
    MOSTRAR_MANO_NUMERO_JUGADOR,
    MOSTRAR_MANO_COLOR_JUGADOR,

    // Estados relacionados con el progreso del juego y sus eventos
    MOSTRAR_ESTADO_JUEGO,
    MOSTRAR_TURNO_ACTUAL,
    MOSTRAR_PISTAS_DISPONIBLES,
    MOSTRAR_VIDAS_DISPONIBLES,
    MOSTRAR_ESTADO_FIN_JUEGO,

    // Estado relacionado con el registro de jugadores
    REGISTRO_JUGADOR,
    ERROR_REGISTRO_JUGADOR,
    MOSTRAR_JUGADORES,
    REGISTRAR_JUGADOR,

    // Estados relacionados con la interacción de pistas y cartas
    MOSTRAR_CARTAS_DISPONIBLES,
    MOSTRAR_PISTAS_USADAS,
    MOSTRAR_ACCIONES_JUGADOR,

}
