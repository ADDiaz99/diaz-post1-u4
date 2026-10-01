package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorAreaHandler extends NivelAprobacionHandler {

    private static final double LIMITE_MONTO = 2_000_000;
    private static final String NOMBRE_NIVEL = "Supervisor de Área";

    public SupervisorAreaHandler(NivelAprobacionHandler siguiente) {
        super(siguiente);
    }

    @Override
    protected boolean puedeResolver(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE_MONTO;
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        return new ResultadoAprobacion(true, NOMBRE_NIVEL,
                "Aprobada dentro de la autoridad del Supervisor de Área");
    }
}
