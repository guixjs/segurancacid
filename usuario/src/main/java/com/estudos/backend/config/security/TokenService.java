package com.estudos.backend.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.estudos.backend.exception.TokenInvalidoException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    private static final String SECRET = "chave-secreta";

    public String gerarToken(UserDetails user) {
        try{
            return JWT.create()
                    .withIssuer("back-usuarios")
                    .withSubject(user.getUsername())
                    .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                    .sign(Algorithm.HMAC256(SECRET));
        }catch (JWTCreationException e){
            return "Erro ao criar token: ";
        }

    }

    public String validarToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer("back-usuarios")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new TokenInvalidoException("Token inválido ou expirado");
        }
    }
}
