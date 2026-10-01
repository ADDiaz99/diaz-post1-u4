package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.springframework.stereotype.Service;

/**
 * Fábrica de EjecutorSolicitud ya conectada al NotificadorCambioEstado de la
 * Necesidad 3. EjecutorSolicitud no es un bean en sí (se crea una instancia
 * por Solicitud, con su propio historial), así que este servicio —que sí es
 * un singleton de Spring— es quien recibe las dependencias compartidas por
 * inyección y las usa para construir cada ejecutor.
 */
@Service
public class EjecucionSolicitudService {

    private final PresupuestoService presupuestoService;
    private final OrdenCompraService ordenCompraService;
    private final NotificadorCambioEstado notificador;

    public EjecucionSolicitudService(PresupuestoService presupuestoService, OrdenCompraService ordenCompraService,
                                      NotificadorCambioEstado notificador) {
        this.presupuestoService = presupuestoService;
        this.ordenCompraService = ordenCompraService;
        this.notificador = notificador;
    }

    public EjecutorSolicitud crearEjecutor(Solicitud solicitud) {
        return new EjecutorSolicitud(solicitud, presupuestoService, ordenCompraService, notificador);
    }
}
