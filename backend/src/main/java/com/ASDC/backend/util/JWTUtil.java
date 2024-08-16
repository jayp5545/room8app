package com.ASDC.backend.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiry;

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(SignatureAlgorithm.HS512,getSecretKey())
                .compact();
    }
    private Key getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    private boolean checkTokenExpired(String token){
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    private Date getExpiration(String token){
        return getAllClaimsFromToken(token).getExpiration();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }

}
