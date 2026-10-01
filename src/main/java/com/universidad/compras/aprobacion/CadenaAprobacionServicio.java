package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.springframework.stereotype.Service;

/**
 * Patrón: Chain of Responsibility (configuración de la cadena).
 *
 * Esta es la única clase que conoce cuántos niveles existen y en qué orden
 * se consultan. ControladorSolicitudes solo conoce ServicioAprobacion, así
 * que agregar, quitar o reordenar un nivel —como el Revisor de Cumplimiento
 * Normativo, que ya se añadió como primer eslabón— se hace enteramente
 * aquí, sin tocar el controlador ni los demás niveles.
 *
 * Se descartó Command puro para esta necesidad (ver README, Necesidad 1):
 * el problema central es que una solicitud debe recorrer una secuencia de
 * decisores hasta que uno la resuelve, no encapsular una operación que deba
 * sobrevivir como objeto para deshacerse más tarde (eso es la Necesidad 2).
 *
 * Integra el Observer de la Necesidad 3: el cambio de estado de la
 * solicitud se delega a NotificadorCambioEstado en lugar de llamarse
 * directamente, de modo que esta clase no conoce el correo, el dashboard ni
 * la auditoría.
 */
@Service
public class CadenaAprobacionServicio implements ServicioAprobacion {

    private final NivelAprobacionHandler cadena;
    private final NotificadorCambioEstado notificador;

    public CadenaAprobacionServicio(NotificadorCambioEstado notificador) {
        this.notificador = notificador;
        this.cadena = new RevisorCumplimientoHandler(
                new SupervisorAreaHandler(
                        new GerenteAreaHandler(
                                new DirectorFinancieroHandler())));
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = cadena.evaluar(solicitud);

        solicitud.setNivelResolutor(resultado.getNivelResolutor());
        notificador.cambiarEstado(solicitud, resultado.isAprobada() ? "APROBADA" : "RECHAZADA");

        return resultado;
    }
}
