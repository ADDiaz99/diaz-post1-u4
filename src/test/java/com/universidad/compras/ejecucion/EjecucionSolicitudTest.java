package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    private EjecutorSolicitud nuevoEjecutor(Solicitud s) {
        return new EjecutorSolicitud(s, new PresupuestoService(), new OrdenCompraService(),
                new NotificadorCambioEstado(List.of()));
    }

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = nuevoEjecutor(s);

        ejecutor.reservarPresupuesto();
        ejecutor.generarOrdenCompra("Proveedor XYZ");

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = nuevoEjecutor(s);

        assertDoesNotThrow(() -> {
            ejecutor.reservarPresupuesto();
            ejecutor.generarOrdenCompra("Proveedor ABC");
            ejecutor.deshacerUltima(); // deshace solo la generación de la orden

            List<OperacionEjecutada> historial = ejecutor.getHistorial();
            assertFalse(historial.get(0).isDeshecha()); // la reserva de presupuesto sigue vigente
            assertTrue(historial.get(1).isDeshecha());  // la orden quedó deshecha
        });
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 2000000, "SOFTWARE", "CC-300");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = nuevoEjecutor(s);

        assertDoesNotThrow(() -> {
            ejecutor.reservarPresupuesto();
            ejecutor.generarOrdenCompra("Proveedor QRS");

            List<OperacionEjecutada> historial = ejecutor.getHistorial();
            assertEquals(2, historial.size());
        });
    }
}
