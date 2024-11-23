package ar.edu.unlu.Hanabi.ModeloNew;

public enum Eventos {

    // Eventos relacionados con el inicio del juego
    JUEGO_INICIADO,
    INICIAR_JUEGO,

    // Eventos relacionados con los turnos
    INICIO_TURNO,
    INICIAR_TURNO,
    CAMBIO_TURNO,
    CAMBIAR_TURNO,
    NO_ES_TURNO,

    // Eventos relacionados con el resultado del juego
    VICTORIA,
    DERROTA,

    // Eventos relacionados con el uso de las fichas
    FICHA_PISTA_USADA,
    FICHA_VIDA_PERDIDA,
    FICHA_PISTA_RECUPERADA,
    NO_HAY_PISTA_DIPONIBLE,
    NO_HAY_FICHA_VIDA,
    NO_HAY_PISTAS_USADAS,
    FALTA_FICHA_PISTA,
    FALTA_FICHA_VIDA,
    FALTA_FICHA_PISTA_USADA,


    // Eventos relacionados con la acción de los jugadores
    JUGADOR_JUGO_CARTA,
    JUGADOR_DESCARTO_CARTA,
    JUGADOR_TOMO_CARTA,
    JUGADOR_DIO_PISTA,
    JUGAR_CARTA,
    DESCARTAR_CARTA,
    DAR_PISTA,
    PISTA_CREADA,



    // Eventos relacionados con la creación y registro de jugadores
    JUGADOR_REGISTRADO,
    REGISTRAR_JUGADOR,
    ERROR_REGISTRO_JUGADOR,
    JUGADOR_CREADO,
    ERROR_CREACION_JUGADOR,
    ACCION_DESCONOCIDA,
    ERROR_ACCION,

    // Eventos relacionados con la visualización de las manos y la vista del jugador
    MOSTRAR_MANO_JUGADOR_TURNO,
    MOSTRAR_MANOS_JUGADOR_ESPERA,
    MOSTRAR_VISTA_JUGADOR,
    MOSTRAR_MANO,
    MOSTRAR_MANO_NUMEROS,
    MOSTRAR_MANO_COLORES,

    // Eventos relacionados con la obtención de información sobre los jugadores
    OBTENER_NOMBRE_JUGADOR,
    OBTENER_CANTIDAD_JUGADORES,
    OBTENER_JUGADORES,
    JUGADOR_NO_EXISTE,
    MOSTRAR_MANO_JUGADOR_PISTA,

    // Evento cuando un jugador realiza una jugada de carta
    JUGADA_REALIZADA,

    // Evento cuando un jugador da una pista a otro jugador
    PISTA_DADA,

    // Evento cuando un jugador descarta una carta
    CARTA_DESCARTADA,

    // Evento para actualizar la mano de un jugador
    ACTUALIZAR_MANO,

    // Evento que indica que el turno del jugador ha finalizado
    FIN_DE_TURNO,

    // Evento para indicar que el juego ha comenzado


    // Evento para indicar que el juego ha terminado
    JUEGO_TERMINADO,

    // Evento de error o notificación general
    NOTIFICACION_GENERAL,
}

