package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controlador de ejemplo para exponer la ejecución reversible por HTTP.
 * No forma parte del contrato dado por el laboratorio; se incluye para
 * dejar el flujo completo de punta a punta. Mantiene en memoria un
 * EjecutorSolicitud por solicitud para poder deshacer operaciones entre
 * peticiones distintas.
 */
@RestController
@RequestMapping("/api/ejecucion")
public class ControladorEjecucion {

    private final EjecucionSolicitudService ejecucionSolicitudService;
    private final Map<String, EjecutorSolicitud> ejecutoresPorSolicitud = new ConcurrentHashMap<>();

    public ControladorEjecucion(EjecucionSolicitudService ejecucionSolicitudService) {
        this.ejecucionSolicitudService = ejecucionSolicitudService;
    }

    @PostMapping("/{solicitudId}/reservar-presupuesto")
    public ResponseEntity<String> reservarPresupuesto(@PathVariable String solicitudId, @RequestBody Solicitud solicitud) {
        EjecutorSolicitud ejecutor = ejecutoresPorSolicitud.computeIfAbsent(
                solicitudId, id -> ejecucionSolicitudService.crearEjecutor(solicitud));
        ejecutor.reservarPresupuesto();
        return ResponseEntity.ok("Presupuesto reservado");
    }

    @PostMapping("/{solicitudId}/generar-orden")
    public ResponseEntity<String> generarOrden(@PathVariable String solicitudId, @RequestParam String proveedor) {
        EjecutorSolicitud ejecutor = ejecutoresPorSolicitud.get(solicitudId);
        if (ejecutor == null) {
            return ResponseEntity.status(404).body("No existe un ejecutor para la solicitud " + solicitudId);
        }
        ejecutor.generarOrdenCompra(proveedor);
        return ResponseEntity.ok("Orden de compra generada");
    }

    @PostMapping("/{solicitudId}/deshacer-ultima")
    public ResponseEntity<String> deshacerUltima(@PathVariable String solicitudId) {
        EjecutorSolicitud ejecutor = ejecutoresPorSolicitud.get(solicitudId);
        if (ejecutor == null) {
            return ResponseEntity.status(404).body("No existe un ejecutor para la solicitud " + solicitudId);
        }
        ejecutor.deshacerUltima();
        return ResponseEntity.ok("Última operación deshecha");
    }

    @GetMapping("/{solicitudId}/historial")
    public ResponseEntity<List<String>> historial(@PathVariable String solicitudId) {
        EjecutorSolicitud ejecutor = ejecutoresPorSolicitud.get(solicitudId);
        if (ejecutor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ejecutor.getHistorial().stream().map(Object::toString).toList());
    }
}
