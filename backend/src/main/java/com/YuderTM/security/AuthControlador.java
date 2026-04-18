package com.YuderTM.security;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://confienvios-app.onrender.com")
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

        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(username);
        return ResponseEntity.ok(Map.of("token", token, "usuario", username));
    }

    // ✅ Endpoint TEMPORAL para encriptar - ELIMINAR después de usarlo
    @GetMapping("/encriptar/{id}/{password}")
    public ResponseEntity<?> encriptarPassword(
            @PathVariable Integer id,
            @PathVariable String password) {

        Usuario u = userRepository.findById(id).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();

        u.setPassword(passwordEncoder.encode(password));
        userRepository.save(u);

        return ResponseEntity.ok("Contraseña encriptada para usuario ID: " + id);
    }
}
