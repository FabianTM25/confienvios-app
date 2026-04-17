package com.YuderTM.servicio;

import com.YuderTM.modelo.Factura;


import java.util.List;


public interface IFacturaService {


       // Listar todos
        List<Factura> listarFactura();

        // Buscar por ID
        Factura buscarFacturaId(Integer id_factura);

        // Guardar
        Factura guardarFactura (Factura factura);

        // Eliminar
        void eliminarFacturaId(Integer id_factura);

        Long contarFacturasDia();
        Long contarFacturasMes();
        Long contarFacturasAnio();

}
