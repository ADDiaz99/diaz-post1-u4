package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Patrón: Observer (rol Observer/Subscriber).
 *
 * Cada implementación reacciona a un cambio de estado sin que
 * NotificadorCambioEstado (el sujeto) conozca su lógica interna: el sujeto
 * solo sabe que implementa este contrato. Agregar una cuarta reacción
 * significa escribir una nueva clase que implemente esta interfaz y
 * registrarla, sin tocar NotificadorCambioEstado ni las demás reacciones.
 */
public interface SuscriptorCambioEstado {
    void onCambioEstado(Solicitud solicitud);
}
