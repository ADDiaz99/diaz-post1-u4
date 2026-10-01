package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteAreaHandler extends NivelAprobacionHandler {

    private static final double LIMITE_MONTO = 10_000_000;
    private static final String NOMBRE_NIVEL = "Gerente de Área";

    public GerenteAreaHandler(NivelAprobacionHandler siguiente) {
        super(siguiente);
    }

    @Override
    protected boolean puedeResolver(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE_MONTO;
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        return new ResultadoAprobacion(true, NOMBRE_NIVEL,
                "Aprobada dentro de la autoridad del Gerente de Área");
    }
}
