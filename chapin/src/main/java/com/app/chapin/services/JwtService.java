package com.app.chapin.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private static final String SECRET_KEY = "c2VjcmV0X3NlY3JldF9rZXlfZm9yX2p3dC1hbGdvcml0aG0=";
    private static final long TREINTA_DIAS = 30;
    final long ONE_DAY = 1000*60*60*24;
    final long THIRTY_DAYS = 1000*60*60*24*30;

    public String getToken(UserDetails usuario) {
        return getToken(new HashMap<>(), usuario);
    }

    private String getToken(HashMap<String, Object> extraClaims, UserDetails usuario) {
        Date fechaExpiracion = getFechaExpiracion();
        log.info("Token generado con fecha de finalizacion {}", fechaExpiracion);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(fechaExpiracion)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usermame = getUsernameFromToken(token);
        return (usermame.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw e;
        }

    }

    private Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        log.info("Expiracion del token jwt {}", getExpiration(token));
        return getExpiration(token).before(new Date());
    }

    private Date getFechaExpiracion() {
        // Obtener la fecha y hora actuales con LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(TREINTA_DIAS);

        // Convertir LocalDateTime a ZonedDateTime con la zona horaria del sistema
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        // Convertir ZonedDateTime a Date
        Date date = Date.from(zonedDateTime.toInstant());

        System.out.println("LocalDateTime: " + localDateTime);
        System.out.println("Date: " + date);

        return date;
    }
}
