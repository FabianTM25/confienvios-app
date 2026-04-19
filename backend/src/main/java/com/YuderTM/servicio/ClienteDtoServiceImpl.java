package com.YuderTM.servicio;

import com.YuderTM.modelo.Cliente_dto;
import com.YuderTM.repositorio.IClienteDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional

public class ClienteDtoServiceImpl implements IClienteDtoService{

    @Autowired
    private final IClienteDtoRepository iClienteDtoRepository;
    //constructor -inyeccion de dependencias
    public ClienteDtoServiceImpl(IClienteDtoRepository iClienteDtoRepository) {
        this.iClienteDtoRepository = iClienteDtoRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Cliente_dto> listarClienteDto() {
        return iClienteDtoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente_dto buscarPorIdDto(Integer id_clienteDto) {
        return iClienteDtoRepository.findById(id_clienteDto).orElse(null);
    }

    //sirve para crear o actualizar
    @Override
    public Cliente_dto guardarClienteDto(Cliente_dto clienteDto) {
         clienteDto.setNombreClienteDto(clienteDto.getNombreClienteDto().toUpperCase());
        return iClienteDtoRepository.save(clienteDto);
    }

    @Override
    public void eliminarClienteDto(Integer id_clienteDto) {

        if(!iClienteDtoRepository.existsById(id_clienteDto)) {
            throw new NoSuchElementException("Usuario no encontrado " + id_clienteDto);
        }
        iClienteDtoRepository.deleteById(id_clienteDto);
    }

    //buscar
    @Override
    public Cliente_dto buscarDocumento(String documento) {
        return iClienteDtoRepository
                .findByDocumentoClienteDto(documento)
                .orElse(null);
    }

}
