package com.universidad.compras.estado;

// Estado terminal: una solicitud cancelada no admite ninguna otra operación.
public class CanceladaState implements EstadoSolicitud {
    @Override
    public String nombre() { return "CANCELADA"; }
}
