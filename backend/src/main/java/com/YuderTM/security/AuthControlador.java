package com.YuderTM.security;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;

import com.YuderTM.security.JwtUtil;


import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController

/*@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")*/

@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://confienvios-app.onrender.com")

public class AuthControlador {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthControlador(IUserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("usuario");
        String password = credenciales.get("password");

        Usuario usuario = userRepository.findByUsuario(username);

        boolean eliminado = usuario != null && usuario.getEstado() != null && usuario.getEstado() == 2;

        if (usuario == null || eliminado || !passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }

        // Usuarios creados antes de que existiera el campo "rol" quedan en null:
        // se tratan como ADMIN para no perder el acceso que ya tenían.
        String rol = (usuario.getRol() == null || usuario.getRol().isBlank()) ? "ADMIN" : usuario.getRol();

        String token = jwtUtil.generarToken(username, rol);
        return ResponseEntity.ok(Map.of("token", token, "usuario", username, "rol", rol));
    }
}
