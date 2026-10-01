package com.universidad.compras.ejecucion;

import java.time.Instant;

/** Registro de una operación ejecutada, con la marca de tiempo de ejecución, para el historial consultable. */
public class OperacionEjecutada {
    private final OperacionSolicitud operacion;
    private final Instant momentoEjecucion;
    private boolean deshecha;

    public OperacionEjecutada(OperacionSolicitud operacion, Instant momentoEjecucion) {
        this.operacion = operacion;
        this.momentoEjecucion = momentoEjecucion;
        this.deshecha = false;
    }

    public OperacionSolicitud getOperacion() { return operacion; }
    public Instant getMomentoEjecucion() { return momentoEjecucion; }
    public boolean isDeshecha() { return deshecha; }
    void marcarDeshecha() { this.deshecha = true; }

    @Override
    public String toString() {
        return "[" + momentoEjecucion + "] " + operacion.getDescripcion()
                + (deshecha ? " (DESHECHA)" : "");
    }
}
