package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón: Observer (rol Subject/Publisher).
 *
 * Es el único punto donde el cambio de estado de una Solicitud se conecta
 * con las reacciones externas. Ni CadenaAprobacionServicio (Necesidad 1) ni
 * EjecutorSolicitud (Necesidad 2) —los puntos donde una solicitud cambia de
 * estado— conocen el correo, el dashboard o la auditoría: solo conocen esta
 * clase y le delegan el cambio de estado junto con la notificación.
 *
 * Spring inyecta automáticamente, en la lista del constructor, todos los
 * beans que implementan SuscriptorCambioEstado (los tres @Component de este
 * paquete). agregarSuscriptor() permite registrar un cuarto suscriptor en
 * tiempo de ejecución —como hace la prueba con un colector de prueba— sin
 * modificar esta clase ni las tres reacciones existentes.
 */
@Component
public class NotificadorCambioEstado {

    private final List<SuscriptorCambioEstado> suscriptores;

    public NotificadorCambioEstado(List<SuscriptorCambioEstado> suscriptores) {
        this.suscriptores = new ArrayList<>(suscriptores);
    }

    public void agregarSuscriptor(SuscriptorCambioEstado suscriptor) {
        suscriptores.add(suscriptor);
    }

    /**
     * Punto único de cambio de estado: actualiza la Solicitud y notifica a
     * todos los suscriptores registrados en ese momento.
     */
    public void cambiarEstado(Solicitud solicitud, String nuevoEstado) {
        solicitud.setEstado(nuevoEstado);
        for (SuscriptorCambioEstado suscriptor : suscriptores) {
            suscriptor.onCambioEstado(solicitud);
        }
    }
}
