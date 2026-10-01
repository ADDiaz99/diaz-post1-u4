package com.universidad.compras.estado;

/**
 * Traduce el campo estado (String) de la entidad Solicitud —dada tal cual,
 * sin conocer el patrón State— al objeto EstadoSolicitud concreto
 * correspondiente. Es el único lugar que conoce la correspondencia entre el
 * texto persistido y la clase de estado; agregar un estado nuevo exige
 * agregar una línea aquí además de la clase del estado en sí.
 */
public final class EstadoSolicitudFactory {

    private EstadoSolicitudFactory() {}

    public static EstadoSolicitud desde(String nombreEstado) {
        return switch (nombreEstado) {
            case "PENDIENTE" -> new PendienteState();
            case "APROBADA" -> new AprobadaState();
            case "EJECUTADA" -> new EjecutadaState();
            case "RECHAZADA" -> new RechazadaState();
            case "CANCELADA" -> new CanceladaState();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombreEstado);
        };
    }
}
