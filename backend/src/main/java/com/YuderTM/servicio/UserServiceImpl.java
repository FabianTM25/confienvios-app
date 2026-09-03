
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
    return iUserRepository.findAll().stream()
      .filter(u -> u.getEstado() == null || u.getEstado() != 2)
      .toList();
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

      if (usuario.getRol() == null || usuario.getRol().isBlank()) {
        usuario.setRol("USUARIO");
      }

    } else {

      // ACTUALIZACIÓN
      Usuario existente =
        iUserRepository
          .findById(usuario.getId_usuario())
          .orElseThrow(() -> new NoSuchElementException(
            "Usuario no encontrado " + usuario.getId_usuario()
          ));

      String nuevaPassword = usuario.getPassword();

      if (nuevaPassword == null || nuevaPassword.isBlank()) {
        // No se envió una contraseña nueva: se conserva la actual
        usuario.setPassword(existente.getPassword());
      } else {
        usuario.setPassword(
          passwordEncoder.encode(nuevaPassword)
        );
      }
    }

    return iUserRepository.save(usuario);
  }

  // =========================
  // ELIMINAR (soft-delete, igual que Cliente_dto/Cliente_rmt)
  // =========================
  @Override
  public void eliminarUsuarioId(Integer id_usuario) {

    Usuario usuario = iUserRepository
      .findById(id_usuario)
      .orElseThrow(() -> new NoSuchElementException(
        "Usuario no encontrado " + id_usuario
      ));

    usuario.setEstado(2);
    iUserRepository.save(usuario);
  }
}

