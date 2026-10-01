package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Patrón: Chain of Responsibility (clase base del manejador).
 *
 * Cada nivel de aprobación encapsula una única condición de procesamiento
 * (¿puedo resolver esta solicitud?) y, si no puede, delega al siguiente
 * eslabón sin que ninguno de los dos conozca la cadena completa. Esto
 * permite que CadenaAprobacionServicio construya, reordene o extienda la
 * cadena (como el Revisor de Cumplimiento Normativo para solicitudes
 * INTERNACIONAL) sin modificar ControladorSolicitudes ni los demás niveles:
 * cada NivelAprobacionHandler solo conoce al siguiente, nunca a todos.
 */
public abstract class NivelAprobacionHandler {

    private final NivelAprobacionHandler siguiente;

    protected NivelAprobacionHandler(NivelAprobacionHandler siguiente) {
        this.siguiente = siguiente;
    }

    public final ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (puedeResolver(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.evaluar(solicitud);
        }
        throw new IllegalStateException(
                "Ningún nivel de aprobación pudo resolver la solicitud " + solicitud.getId());
    }

    protected abstract boolean puedeResolver(Solicitud solicitud);

    protected abstract ResultadoAprobacion resolver(Solicitud solicitud);
}
