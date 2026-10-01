package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaSuscriptor implements SuscriptorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.registrarAuditoria(
                solicitud.getId(), solicitud.getEstado(),
                "Resuelto por " + solicitud.getNivelResolutor());
    }
}
