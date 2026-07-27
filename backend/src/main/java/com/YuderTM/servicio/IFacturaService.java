package com.YuderTM.servicio;

import com.YuderTM.modelo.Factura;


import java.util.List;


public interface IFacturaService {


       // Listar todos
        List<Factura> listarFactura();

        // Buscar por ID
        Factura buscarFacturaId(Integer id_factura);

        // Buscar por numero de factura
        Factura buscarFacturaPorNumero(String numero_factura);

        // Guardar
        Factura guardarFactura (Factura factura);

        // Eliminar
        void eliminarFacturaId(Integer id_factura);

        Long contarFacturasDia();
        Long contarFacturasMes();
        Long contarFacturasAnio();


        boolean existeEncomiendaClienteMes(String documentoClienteDto);


}
