package com.YuderTM.servicio;

import com.YuderTM.modelo.Cliente_dto;
<<<<<<< HEAD
import com.YuderTM.modelo.Cliente_rmt;
=======
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f

import java.util.List;

public interface IClienteDtoService {

        List<Cliente_dto> listarClienteDto();

        Cliente_dto buscarPorIdDto(Integer idClienteDto);

        Cliente_dto guardarClienteDto(Cliente_dto cliente_dto);

        void eliminarClienteDto(Integer idClienteDto);

        Cliente_dto buscarDocumento(String texto);
    }

