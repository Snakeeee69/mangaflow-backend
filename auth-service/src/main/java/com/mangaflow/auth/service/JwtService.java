package com.mangaflow.auth.service;

import com.mangaflow.auth.model.Credencial;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:MangaFlowSuperSecretKeyParaJWT2026DuocUCFullStack2!}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    public String generarToken(Credencial credencial) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(credencial.getCorreo())
                .claim("personaId", credencial.getPersonaId())
                .claim("rol", credencial.getRol().name())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(key)
                .compact();
    }
}