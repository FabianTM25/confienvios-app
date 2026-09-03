package com.YuderTM.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${JWT_SECRET}")
  private String secretKey;

  private Key getKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  private final long EXPIRATION = 1000 * 60 * 60 * 8;

  public String generarToken(String username, String rol) {
    return Jwts.builder()
      .setSubject(username)
      .claim("rol", rol)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
      .signWith(getKey())
      .compact();
  }

  public String extraerUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build()
      .parseClaimsJws(token).getBody().getSubject();
  }

  public String extraerRol(String token) {
    Object rol = Jwts.parserBuilder().setSigningKey(getKey()).build()
      .parseClaimsJws(token).getBody().get("rol");
    return rol != null ? rol.toString() : null;
  }

  public boolean validarToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}
