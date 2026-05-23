package com.YuderTM.servicio;

<<<<<<< HEAD

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

=======
import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f

@Service
@Transactional
public class UserServiceImpl implements IUserService {

<<<<<<< HEAD
    @Autowired
    private final IUserRepository iUserRepository;
//constructor -inyeccion de dependencias
    public UserServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

=======
  
    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor -inyeccion de dependencias
    public UserServiceImpl(IUserRepository iUserRepository,
            PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f

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

<<<<<<< HEAD
    //sirve para crear o actualizar
    @Override
    public Usuario guardarUsuario(Usuario usuario) {
=======
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

>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
        return iUserRepository.save(usuario);
    }

    @Override
<<<<<<< HEAD
    public void eliminarUsuarioId(Integer id_usuario) {

        if(!iUserRepository.existsById(id_usuario)) {
            throw new NoSuchElementException("Usuario no encontrado " + id_usuario);
        }
        iUserRepository.deleteById(id_usuario);
=======
    public void eliminarUsuarioId(Integer id) {
        iUserRepository.deleteById(id);
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
    }

}