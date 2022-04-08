package com.revature.service;


import com.revature.model.User;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;


public class JWTService {

    private Key key;
    public JWTService(){
        byte[] secret = "my_secret_password_asdfasdfjkljclkvjl13432k2312jlkj3941809df".getBytes();
        key = Keys.hmacShaKeyFor(secret);
    }

    public String createJWT(User user) {

        String jwt = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getId())
                .claim("user_role_id", user.getUserRole().getId())
                .signWith(key)
                .compact();
        return jwt;
    }

    public Jws<Claims> parseJwt(String jwt) {
        try {
            Jws<Claims> token = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);

            return token;
        } catch(SignatureException e) {
            throw new UnauthorizedResponse("Invalid JWT\n" + e.getMessage());
        } catch(JwtException e) {
            throw new UnauthorizedResponse("Invalid JWT\n" + e.getMessage());
        }
    }
}