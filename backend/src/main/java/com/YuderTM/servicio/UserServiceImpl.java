package com.YuderTM.servicio;


import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private final IUserRepository iUserRepository;
//constructor -inyeccion de dependencias
    public UserServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return iUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarUsuarioId(Integer id_usuario) {
        return iUserRepository.findById(id_usuario).orElse(null);
    }

    //sirve para crear o actualizar
    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return iUserRepository.save(usuario);
    }

    @Override
    public void eliminarUsuarioId(Integer id_usuario) {

        if(!iUserRepository.existsById(id_usuario)) {
            throw new NoSuchElementException("Usuario no encontrado " + id_usuario);
        }
        iUserRepository.deleteById(id_usuario);
    }

}