package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Patrón: Command (caretaker, un objeto por Solicitud).
 *
 * A diferencia del undo/redo clásico descrito en el pre-contenido —una
 * pila donde solo se puede deshacer el último comando ejecutado—, este caso
 * exige deshacer reservar presupuesto o generar la orden de forma
 * independiente, sin importar cuál se ejecutó después. Por eso el
 * historial aquí es una lista direccionable (cada OperacionEjecutada se
 * puede pedir deshacer por sí misma con deshacer(OperacionEjecutada)), en
 * lugar de una pila que solo expone el tope. El historial conserva todas
 * las operaciones, deshechas o no, para que puedan inspeccionarse después
 * (requisito explícito de la Necesidad 2), y deshacerUltima() se ofrece
 * además como atajo para el caso de uso más común.
 *
 * Se descartó para esta necesidad el patrón de la Necesidad 1 (Chain of
 * Responsibility): no hay aquí ningún decisor evaluando condiciones para
 * decidir si delega o resuelve una solicitud entrante. Reservar presupuesto
 * y generar orden son dos operaciones discretas que el mismo actor (el
 * equipo de Compras) decide ejecutar, y que deben registrarse para poder
 * revertirse más tarde — eso es exactamente lo que Command modela y CoR no.
 */
public class EjecutorSolicitud {

    private final Solicitud solicitud;
    private final PresupuestoService presupuestoService;
    private final OrdenCompraService ordenCompraService;
    private final NotificadorCambioEstado notificador;
    private final List<OperacionEjecutada> historial = new ArrayList<>();

    public EjecutorSolicitud(Solicitud solicitud, PresupuestoService presupuestoService,
                              OrdenCompraService ordenCompraService, NotificadorCambioEstado notificador) {
        this.solicitud = solicitud;
        this.presupuestoService = presupuestoService;
        this.ordenCompraService = ordenCompraService;
        this.notificador = notificador;
    }

    public void reservarPresupuesto() {
        OperacionSolicitud operacion = new ReservarPresupuestoCommand(
                presupuestoService, solicitud.getCentroCosto(), solicitud.getMonto());
        ejecutarYRegistrar(operacion);
    }

    public void generarOrdenCompra(String proveedor) {
        OperacionSolicitud operacion = new GenerarOrdenCompraCommand(
                ordenCompraService, solicitud.getId(), proveedor);
        ejecutarYRegistrar(operacion);
        notificador.cambiarEstado(solicitud, "EJECUTADA");
    }

    private void ejecutarYRegistrar(OperacionSolicitud operacion) {
        operacion.ejecutar();
        historial.add(new OperacionEjecutada(operacion, Instant.now()));
    }

    /** Deshace la última operación ejecutada que no haya sido deshecha todavía. */
    public void deshacerUltima() {
        for (int i = historial.size() - 1; i >= 0; i--) {
            OperacionEjecutada registro = historial.get(i);
            if (!registro.isDeshecha()) {
                deshacer(registro);
                return;
            }
        }
        throw new IllegalStateException("No hay operaciones pendientes por deshacer para la solicitud "
                + solicitud.getId());
    }

    /** Deshace una operación específica del historial, sin afectar a las demás. */
    public void deshacer(OperacionEjecutada registro) {
        if (registro.isDeshecha()) {
            throw new IllegalStateException("La operación ya fue deshecha: " + registro.getOperacion().getDescripcion());
        }
        registro.getOperacion().deshacer();
        registro.marcarDeshecha();
    }

    /** Historial completo y ordenado de operaciones ejecutadas sobre esta solicitud, no solo la última. */
    public List<OperacionEjecutada> getHistorial() {
        return List.copyOf(historial);
    }
}
