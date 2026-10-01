package com.universidad.compras.ejecucion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PresupuestoService y OrdenCompraService son clases planas sin anotaciones
 * Spring y no pueden modificarse. Esta configuración las registra como
 * beans para que EjecucionSolicitudService pueda recibirlas por inyección,
 * sin tocar el código de ninguna de las dos.
 */
@Configuration
public class EjecucionConfig {

    @Bean
    public PresupuestoService presupuestoService() {
        return new PresupuestoService();
    }

    @Bean
    public OrdenCompraService ordenCompraService() {
        return new OrdenCompraService();
    }
}
