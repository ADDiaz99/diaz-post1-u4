package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Nivel adicional para solicitudes INTERNACIONAL. Se ubica como primer
 * eslabón de la cadena para que la validación de cumplimiento normativo
 * ocurra antes que cualquier nivel basado en monto. En esta versión del
 * laboratorio, la revisión de cumplimiento es la condición de negocio que
 * determina por completo la resolución de una solicitud internacional
 * (toda solicitud internacional requiere ese visto bueno regulatorio,
 * independientemente de su monto), por lo que este nivel resuelve y no
 * delega. Las solicitudes no internacionales no son de su competencia y
 * se delegan de inmediato al siguiente nivel, sin evaluarlas.
 */
public class RevisorCumplimientoHandler extends NivelAprobacionHandler {

    private static final String CATEGORIA_INTERNACIONAL = "INTERNACIONAL";
    private static final String NOMBRE_NIVEL = "Revisor de Cumplimiento Normativo";

    public RevisorCumplimientoHandler(NivelAprobacionHandler siguiente) {
        super(siguiente);
    }

    @Override
    protected boolean puedeResolver(Solicitud solicitud) {
        return CATEGORIA_INTERNACIONAL.equals(solicitud.getCategoria());
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        return new ResultadoAprobacion(true, NOMBRE_NIVEL,
                "Cumplimiento normativo validado para solicitud internacional");
    }
}
