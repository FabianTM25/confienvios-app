package com.YuderTM.servicio;

import com.YuderTM.modelo.Usuario;
import java.util.List;



public interface IUserService {


       // Listar todos
        List<Usuario> listarUsuarios();

        // Buscar por ID
        Usuario buscarUsuarioId(Integer id_usuario);

        // Guardar
        Usuario guardarUsuario (Usuario usuario);

        // Actualizar
        //User actualizarUsuario(User user);

        // Eliminar
        void eliminarUsuarioId(Integer id_usuario);

}
