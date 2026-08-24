package com.YuderTM.servicio;

import com.YuderTM.modelo.Venta;

import java.math.BigDecimal;
import java.util.List;

public interface IVentaService {

    List<Venta> listarVenta();

    Venta buscarVentaId(Integer id_venta);

    // Crea la venta a partir de una factura ya existente (por numero), el peso y si lleva caja.
    // El calculo del valor se hace en el servidor, nunca se confia en un valor que mande el cliente.
    Venta crearVenta(
            String numero_factura,
            BigDecimal peso,
            boolean con_caja,
            String observaciones,
            BigDecimal valor_avaluo,
            String forma_pago
    );

    void eliminarVentaId(Integer id_venta);

    Venta anularVenta(Integer id_venta);
}
