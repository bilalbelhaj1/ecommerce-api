package org.example.ecommerceapi.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Role;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(AppUser appUser) {
        return Jwts.builder()
                .setSubject(appUser.getUsername())
                .claim("roles",
                        appUser.getRoles().stream()
                                .map(Role::getName)
                                .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}