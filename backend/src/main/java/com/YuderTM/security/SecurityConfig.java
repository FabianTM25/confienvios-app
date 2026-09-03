/* package com.YuderTM.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean

   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/**//*.js",
                                "/**//*.css",
                                "/assets/**",
                                "/api/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }*/


package com.YuderTM.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

  @Configuration
  public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
      this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .authorizeHttpRequests(auth -> auth

          // permitir preflight CORS
          .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

          // públicos
          .requestMatchers(
            "/",
            "/index.html",
            "/favicon.ico",
            "/**/*.js",
            "/**/*.css",
            "/assets/**",
            "/api/auth/login",
            "/api/clienteDto/buscar",  // ← agrega
            "/api/clientes/buscar"
          ).permitAll()

          // gestion de usuarios: solo administradores
          .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")

          // protegidos
          .requestMatchers("/api/**").authenticated()

          // otros
          .anyRequest().permitAll()
        )

        .addFilterBefore(
          jwtFilter,
          UsernamePasswordAuthenticationFilter.class
        );

      return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

      CorsConfiguration config = new CorsConfiguration();

      config.setAllowedOrigins(List.of(
        "http://localhost:4200",
        "https://confienvios-app-two.vercel.app",
        "https://confienvios-n2pn5wypp-fabiantm25s-projects.vercel.app",  // ← agrega este
        "https://confienvios-app.vercel.app"  // ← agrega este
      ));

      config.setAllowedMethods(List.of(
        "GET",
        "POST",
        "PUT",
        "PATCH",
        "DELETE",
        "OPTIONS"
      ));

      config.setAllowedHeaders(List.of("*"));

      config.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

      source.registerCorsConfiguration("/**", config);

      return source;
    }
  }
