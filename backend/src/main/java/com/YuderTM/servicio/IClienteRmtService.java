package com.YuderTM.servicio;

import com.YuderTM.modelo.Cliente_rmt;
import java.util.List;

public interface IClienteRmtService {

        List<Cliente_rmt> listarClienteRtm();

        Cliente_rmt buscarPorIdRtm(Integer idClienteRmt);

        Cliente_rmt guardarClienteRtm(Cliente_rmt cliente_rmt);

        void eliminarClienteRtm(Integer idClienteRmt);

        Cliente_rmt buscarDocumento(String texto);
    }

