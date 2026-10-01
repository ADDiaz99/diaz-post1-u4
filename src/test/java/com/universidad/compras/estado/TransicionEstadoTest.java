package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransicionEstadoTest {

    @Test
    void ejecutarUnaSolicitudAprobadaLaDejaEjecutada() {
        Solicitud s = new Solicitud("S-030", "luis@udes.edu.co", 3000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        ContextoSolicitud contexto = new ContextoSolicitud(s);

        contexto.ejecutar();

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void ejecutarUnaSolicitudPendienteSeRechazaSinCambiarElEstado() {
        Solicitud s = new Solicitud("S-031", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        ContextoSolicitud contexto = new ContextoSolicitud(s);

        contexto.ejecutar();

        assertEquals("PENDIENTE", s.getEstado());
    }

    @Test
    void unaSolicitudEjecutadaNoPuedeVolverAEjecutarse() {
        Solicitud s = new Solicitud("S-032", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        s.setEstado("EJECUTADA");
        ContextoSolicitud contexto = new ContextoSolicitud(s);

        contexto.ejecutar();

        assertEquals("EJECUTADA", s.getEstado());
    }
}
