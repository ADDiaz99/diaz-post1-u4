package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class NotificacionCorreoSuscriptor implements SuscriptorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.enviarCorreo(
                solicitud.getSolicitanteEmail(),
                "Actualización de su solicitud " + solicitud.getId(),
                "Su solicitud cambió al estado " + solicitud.getEstado());
    }
}
