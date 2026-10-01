package com.universidad.compras.ejecucion;

/**
 * Patrón: Command.
 *
 * Cada operación (reservar presupuesto, generar orden de compra) se
 * convierte en un objeto que sabe ejecutarse y deshacerse a sí mismo. Esto
 * es lo que permite que EjecutorSolicitud trate ambas operaciones de forma
 * uniforme —ejecutarlas, deshacerlas de forma independiente, conservarlas
 * en un historial— sin conocer los detalles internos de PresupuestoService
 * ni de OrdenCompraService.
 */
public interface OperacionSolicitud {
    void ejecutar();
    void deshacer();
    String getDescripcion();
}
