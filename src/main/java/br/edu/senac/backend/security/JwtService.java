package br.edu.senac.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;
    private static final long EXPIRATION = 86400000; // 24h em ms
    private static final long EXPIRATION_RESET = 1800000; // 30min em ms

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String gerarToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean tokenValido(String token, UserDetails user) {
        return extrairEmail(token).equals(user.getUsername());
    }

    // -------------------------------------------------------
    // Métodos exclusivos para reset de senha
    // -------------------------------------------------------

    public String gerarTokenReset(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "reset")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_RESET))
                .signWith(getKey())
                .compact();
    }

    public String extrairEmailDoTokenReset(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (!"reset".equals(claims.get("type"))) {
                throw new JwtException("Token inválido para reset de senha");
            }

            return claims.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }
}