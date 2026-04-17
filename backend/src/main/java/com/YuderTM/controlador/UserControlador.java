package com.YuderTM.controlador;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.servicio.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuarios", produces = "application/json")//http://localhost:8080/Confienvios
@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://confienvios-app-two.vercel.app/"  // reemplaza con tu URL real de Angular
})
public class UserControlador {

    private final IUserService iUserService;

    private static final Logger logger = LoggerFactory.getLogger(UserControlador.class);

    //inyeccion de dependencia
    public UserControlador(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    // =============================
    // LISTAR TODOS
    // =============================
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = iUserService.listarUsuarios();
        logger.info("Usuarios encontrados: " + usuarios);
        //usuarios.forEach(usuario -> logger.info(usuario.toString()));//imprimir por consola
        return ResponseEntity.ok(iUserService.listarUsuarios());
    }

    // =============================
    // BUSCAR POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {

        Usuario usuario = iUserService.buscarUsuarioId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    // =============================
    // CREAR USUARIO
    // =============================
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(iUserService.guardarUsuario(usuario));
    }

    // =============================
    // ACTUALIZAR USUARIO
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody Usuario usuario) {

        usuario.setId_usuario(id);

        return ResponseEntity.ok(iUserService.guardarUsuario(usuario));
    }

    // =============================
    // ELIMINAR USUARIO
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {

        iUserService.eliminarUsuarioId(id);

        return ResponseEntity.noContent().build();
    }


}