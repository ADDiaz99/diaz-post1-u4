package com.universidad.compras.estado;

public class AprobadaState implements EstadoSolicitud {

    @Override
    public String nombre() { return "APROBADA"; }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EjecutadaState());
        return "Ejecutada";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new CanceladaState());
        return "Cancelada";
    }
}
