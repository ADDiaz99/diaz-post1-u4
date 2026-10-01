package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

/**
 * Patrón: State (rol Context).
 *
 * ContextoSolicitud mantiene la referencia al estado actual y le delega
 * cada operación; es el propio objeto de estado concreto quien decide, al
 * resolver una operación válida, a qué estado siguiente transicionar
 * (cambiarEstado), sin que ContextoSolicitud contenga ningún if/else sobre
 * el nombre del estado. Esto reemplaza el fragmento disperso original
 * (if/else repetido en varios métodos revisando getEstado()) por una única
 * fuente de verdad: el conjunto de reglas vive en las clases de estado.
 *
 * Se descartó Strategy para esta necesidad (ver README, Necesidad 4): aquí
 * no hay un cliente externo que elija e inyecte el comportamiento activo
 * desde afuera en cada llamada —como el carrito de descuentos de la guía—
 * sino la propia solicitud decidiendo, según en qué estado se encuentra,
 * qué operaciones admite y a qué estado pasar luego. Esa capacidad de
 * transicionar de un estado a otro como parte de resolver la operación es
 * precisamente lo que un conjunto de estrategias independientes entre sí no
 * hace por su cuenta.
 */
public class ContextoSolicitud {

    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public ContextoSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = EstadoSolicitudFactory.desde(solicitud.getEstado());
    }

    public String aprobar() { return estadoActual.aprobar(this); }
    public String rechazar() { return estadoActual.rechazar(this); }
    public String ejecutar() { return estadoActual.ejecutar(this); }
    public String cancelar() { return estadoActual.cancelar(this); }

    public String getEstadoActual() { return estadoActual.nombre(); }

    /** Invocado únicamente por las clases de estado concretas al resolver una transición válida. */
    void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.solicitud.setEstado(nuevoEstado.nombre());
    }
}
