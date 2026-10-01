package com.universidad.compras.estado;

public class PendienteState implements EstadoSolicitud {

    @Override
    public String nombre() { return "PENDIENTE"; }

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new AprobadaState());
        return "Aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new RechazadaState());
        return "Rechazada";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new CanceladaState());
        return "Cancelada";
    }
}
