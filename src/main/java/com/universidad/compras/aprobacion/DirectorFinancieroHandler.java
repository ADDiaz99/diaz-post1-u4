package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Último eslabón de la cadena: el Director Financiero no tiene límite
 * superior, por lo que siempre puede resolver cualquier solicitud que haya
 * llegado hasta aquí sin ser resuelta por un nivel anterior.
 */
public class DirectorFinancieroHandler extends NivelAprobacionHandler {

    private static final String NOMBRE_NIVEL = "Director Financiero";

    public DirectorFinancieroHandler() {
        super(null);
    }

    @Override
    protected boolean puedeResolver(Solicitud solicitud) {
        return true;
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        return new ResultadoAprobacion(true, NOMBRE_NIVEL,
                "Aprobada por el Director Financiero (sin límite superior)");
    }
}
