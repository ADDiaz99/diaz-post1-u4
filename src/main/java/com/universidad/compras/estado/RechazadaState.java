package com.universidad.compras.estado;

// Estado terminal: una solicitud rechazada no admite ninguna otra operación.
public class RechazadaState implements EstadoSolicitud {
    @Override
    public String nombre() { return "RECHAZADA"; }
}
