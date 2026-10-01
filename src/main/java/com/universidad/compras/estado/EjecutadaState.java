package com.universidad.compras.estado;

// Estado terminal: ninguna operación adicional es válida sobre una
// solicitud ya ejecutada (ni siquiera volver a ejecutarla).
public class EjecutadaState implements EstadoSolicitud {
    @Override
    public String nombre() { return "EJECUTADA"; }
}
