package com.project.fitness.securty;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private String jwtSecret = "mySuperSecretKeyForFitnessTrackerApplication123456";
    private int jwtExpirations = 172800000;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

    public String generateToken(String userId, String role){
        return Jwts.builder()
                .subject(userId)
                .claim("roles", List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirations))
                .signWith(key())
                .compact();
    }

    public boolean validateJwtToken(String jwtToken){

        try {

            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(jwtToken);

            return true;


        } catch(Exception e){

            return false;

        }
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserIdFromToken(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
