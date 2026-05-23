
  package com.YuderTM.servicio;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

  private final IUserRepository iUserRepository;
  private final PasswordEncoder passwordEncoder;

  // constructor - inyección de dependencias
  public UserServiceImpl(
    IUserRepository iUserRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.iUserRepository = iUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // =========================
  // LISTAR
  // =========================
  @Override
  @Transactional(readOnly = true)
  public List<Usuario> listarUsuarios() {
    return iUserRepository.findAll();
  }

  // =========================
  // BUSCAR POR ID
  // =========================
  @Override
  @Transactional(readOnly = true)
  public Usuario buscarUsuarioId(Integer id_usuario) {
    return iUserRepository
      .findById(id_usuario)
      .orElse(null);
  }

  // =========================
  // GUARDAR / ACTUALIZAR
  // =========================
  @Override
  public Usuario guardarUsuario(Usuario usuario) {

    // NUEVO USUARIO
    if (usuario.getId_usuario() == null) {

      usuario.setPassword(
        passwordEncoder.encode(
          usuario.getPassword()
        )
      );

    } else {

      // ACTUALIZACIÓN
      Usuario existente =
        iUserRepository
          .findById(usuario.getId_usuario())
          .orElse(null);

      if (existente != null &&
        !usuario.getPassword()
          .equals(existente.getPassword())) {

        usuario.setPassword(
          passwordEncoder.encode(
            usuario.getPassword()
          )
        );
      }
    }

    return iUserRepository.save(usuario);
  }

  // =========================
  // ELIMINAR
  // =========================
  @Override
  public void eliminarUsuarioId(Integer id_usuario) {

    if (!iUserRepository.existsById(id_usuario)) {

      throw new NoSuchElementException(
        "Usuario no encontrado " + id_usuario
      );
    }

    iUserRepository.deleteById(id_usuario);
  }
}

