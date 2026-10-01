package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado(
                List.of(new NotificacionCorreoSuscriptor(), new DashboardContabilidadSuscriptor(), new AuditoriaSuscriptor()));

        assertDoesNotThrow(() -> mecanismo.cambiarEstado(s, "APROBADA"));
        assertEquals("APROBADA", s.getEstado());
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1800000, "MATERIAL_OFICINA", "CC-200");
        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado(List.of());

        List<String> colector = new ArrayList<>();
        SuscriptorCambioEstado colectorPrueba = solicitud -> colector.add(solicitud.getEstado());
        mecanismo.agregarSuscriptor(colectorPrueba);

        assertDoesNotThrow(() -> mecanismo.cambiarEstado(s, "RECHAZADA"));
        assertEquals(1, colector.size());
        assertEquals("RECHAZADA", colector.get(0));
    }
}
