package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class DashboardContabilidadSuscriptor implements SuscriptorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.actualizarDashboardContabilidad(
                solicitud.getId(), solicitud.getEstado(), solicitud.getMonto());
    }
}
