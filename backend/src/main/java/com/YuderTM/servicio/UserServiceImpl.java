package com.YuderTM.servicio;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

  
    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor -inyeccion de dependencias
    public UserServiceImpl(IUserRepository iUserRepository,
            PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
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

    // sirve para crear o actualizar
    @Override

    public Usuario guardarUsuario(Usuario usuario) {

        if (usuario.getId_usuario() == null) {
            // nuevo usuario
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            // actualización
            Usuario existente = iUserRepository.findById(usuario.getId_usuario()).orElse(null);

            if (existente != null && !usuario.getPassword().equals(existente.getPassword())) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        return iUserRepository.save(usuario);
    }

    @Override
    public void eliminarUsuarioId(Integer id) {
        iUserRepository.deleteById(id);
    }

}