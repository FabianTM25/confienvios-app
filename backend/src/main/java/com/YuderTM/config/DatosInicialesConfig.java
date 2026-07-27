package com.YuderTM.config;

import com.YuderTM.modelo.Usuario;
import com.YuderTM.repositorio.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// Crea un usuario administrador por defecto la primera vez que la app
// arranca contra una base de datos vacía (instalación nueva de un cliente).
// Solo actúa si no existe ningún usuario todavía, así que no vuelve a
// ejecutarse una vez que ya hay al menos un usuario cargado.
@Configuration
public class DatosInicialesConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatosInicialesConfig.class);

    @Bean
    CommandLineRunner crearUsuarioAdminInicial(
        IUserRepository iUserRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (iUserRepository.count() == 0) {

                Usuario admin = new Usuario();
                admin.setUsuario("admin");
                admin.setUser_name("Administrador");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEstado(1);

                iUserRepository.save(admin);

                logger.info("Usuario administrador inicial creado (usuario: admin). Cambie la contraseña por defecto lo antes posible.");
            }
        };
    }
}
