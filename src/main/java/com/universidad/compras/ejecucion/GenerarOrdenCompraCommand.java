package com.universidad.compras.ejecucion;

public class GenerarOrdenCompraCommand implements OperacionSolicitud {

    private final OrdenCompraService ordenCompraService;
    private final String solicitudId;
    private final String proveedor;
    private String numeroOrdenGenerado;

    public GenerarOrdenCompraCommand(OrdenCompraService ordenCompraService, String solicitudId, String proveedor) {
        this.ordenCompraService = ordenCompraService;
        this.solicitudId = solicitudId;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        this.numeroOrdenGenerado = ordenCompraService.generar(solicitudId, proveedor);
    }

    @Override
    public void deshacer() {
        if (numeroOrdenGenerado == null) {
            throw new IllegalStateException("No se puede deshacer una orden que nunca se generó");
        }
        ordenCompraService.cancelar(numeroOrdenGenerado);
    }

    @Override
    public String getDescripcion() {
        return "Generación de orden de compra para " + solicitudId + " con proveedor " + proveedor;
    }
}
