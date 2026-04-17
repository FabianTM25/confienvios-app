package com.YuderTM.servicio;

import com.YuderTM.modelo.Cliente_dto;

import java.util.List;

public interface IClienteDtoService {

        List<Cliente_dto> listarClienteDto();

        Cliente_dto buscarPorIdDto(Integer idClienteDto);

        Cliente_dto guardarClienteDto(Cliente_dto cliente_dto);

        void eliminarClienteDto(Integer idClienteDto);

        Cliente_dto buscarDocumento(String texto);
    }

