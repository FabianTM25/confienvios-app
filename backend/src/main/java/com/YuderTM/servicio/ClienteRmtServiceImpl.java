package com.YuderTM.servicio;

import com.YuderTM.modelo.Cliente_rmt;
import com.YuderTM.repositorio.IClienteRmtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional

public class ClienteRmtServiceImpl implements IClienteRmtService{

    @Autowired
    private final IClienteRmtRepository iClienteRmtRepository;
    //constructor -inyeccion de dependencias
    public ClienteRmtServiceImpl(IClienteRmtRepository iClienteRmtRepository) {
        this.iClienteRmtRepository = iClienteRmtRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Cliente_rmt> listarClienteRtm() {
        return iClienteRmtRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente_rmt buscarPorIdRtm(Integer id_clienteRmt) {
        return iClienteRmtRepository.findById(id_clienteRmt).orElse(null);
    }

    //sirve para crear o actualizar
    @Override
    public Cliente_rmt guardarClienteRtm(Cliente_rmt clienteRmt) {
            clienteRmt.setNombreClienteRmt(clienteRmt.getNombreClienteRmt().toUpperCase());
        return iClienteRmtRepository.save(clienteRmt);
    }

    @Override
    public void eliminarClienteRtm(Integer id_clienteRmt) {

        if(!iClienteRmtRepository.existsById(id_clienteRmt)) {
            throw new NoSuchElementException("Usuario no encontrado " + id_clienteRmt);
        }
        iClienteRmtRepository.deleteById(id_clienteRmt);
    }
    //buscar
    @Override
    public Cliente_rmt buscarDocumento(String documento) {
        return iClienteRmtRepository
                .findByDocumentoClienteRmt(documento)
                .orElse(null);
    }

}
