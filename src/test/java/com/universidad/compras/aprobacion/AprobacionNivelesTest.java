package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AprobacionNivelesTest {

    private ServicioAprobacion nuevoServicio() {
        return new CadenaAprobacionServicio(new NotificadorCambioEstado(List.of()));
    }

    @Test
    void solicitudDentroDeAutoridadDelSupervisorSeAprueba() {
        ServicioAprobacion servicio = nuevoServicio();
        Solicitud s = new Solicitud("S-001", "ana@udes.edu.co", 1500000, "MATERIAL_OFICINA", "CC-100");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Supervisor de Área", r.getNivelResolutor());
    }

    @Test
    void solicitudQueSuperaAlSupervisorEscalaAlGerente() {
        ServicioAprobacion servicio = nuevoServicio();
        Solicitud s = new Solicitud("S-002", "luis@udes.edu.co", 6000000, "SOFTWARE", "CC-200");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Gerente de Área", r.getNivelResolutor());
    }

    @Test
    void solicitudInternacionalPasaPorCumplimientoAntesDelNivelPorMonto() {
        ServicioAprobacion servicio = nuevoServicio();
        Solicitud s = new Solicitud("S-003", "gerencia@udes.edu.co", 1000000, "INTERNACIONAL", "CC-300");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertEquals("Revisor de Cumplimiento Normativo", r.getNivelResolutor());
    }

    @Test
    void solicitudSinLimiteEscalaHastaElDirectorFinanciero() {
        ServicioAprobacion servicio = nuevoServicio();
        Solicitud s = new Solicitud("S-004", "cfo@udes.edu.co", 50000000, "SOFTWARE", "CC-400");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Director Financiero", r.getNivelResolutor());
    }
}
