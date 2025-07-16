package lk.jiat.bank.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("VerySecretKeyForApilageBankingApplication123456!".getBytes());
    private static final long EXPIRATION_MS = 3600_000; // 1 hour

    public static String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public static boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public static String getUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public static Set<String> getRoles(String token) {
        Object roles = parseToken(token).getBody().get("roles");
        return Set.copyOf((java.util.List<String>) roles);
    }
}
