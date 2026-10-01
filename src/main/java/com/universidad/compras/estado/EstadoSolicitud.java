package com.universidad.compras.estado;

/**
 * Patrón: State.
 *
 * Cada estado implementa únicamente las operaciones válidas para sí mismo;
 * las no válidas quedan cubiertas por el default de la interfaz, que
 * rechaza la operación sin lanzar excepción y sin cambiar el estado de la
 * solicitud (igual que el fragmento disperso original, pero sin repetir la
 * condición en cada método). Agregar un estado nuevo —como
 * EN_ESPERA_PROVEEDOR, anunciado por el equipo de Compras— significa
 * escribir una clase nueva que implemente esta interfaz y registrarla en
 * EstadoSolicitudFactory, sin tocar ContextoSolicitud ni los demás estados.
 */
public interface EstadoSolicitud {

    String nombre();

    default String aprobar(ContextoSolicitud contexto) {
        return operacionInvalida("aprobar");
    }

    default String rechazar(ContextoSolicitud contexto) {
        return operacionInvalida("rechazar");
    }

    default String ejecutar(ContextoSolicitud contexto) {
        return operacionInvalida("ejecutar");
    }

    default String cancelar(ContextoSolicitud contexto) {
        return operacionInvalida("cancelar");
    }

    private String operacionInvalida(String operacion) {
        return "Error: no se puede " + operacion + " una solicitud en estado " + nombre();
    }
}
