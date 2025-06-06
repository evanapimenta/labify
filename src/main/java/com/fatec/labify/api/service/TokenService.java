package com.fatec.labify.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.*;
import com.fatec.labify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) throws BaseException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Labify")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(accessTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new TokenGenerationException("Erro ao gerar o token. Tente novamente");
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Labify")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(refreshTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new TokenGenerationException("Erro ao gerar token. Tente novamente");
        }
    }

    @Transactional(readOnly = true)
    public User validateAndGetUserFromToken(String jwtToken) throws BaseException {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Labify")
                    .build();

            decodedJWT = verifier.verify(jwtToken);
            String userId = decodedJWT.getClaim("id").asString();

            if (userId == null) {
                throw new TokenVerificationException("ID do usuário não encontrado no token.");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

            if (!user.getVerified()) {
                throw new UserNotVerifiedException("Usuário não verificado.");
            }

            return user;
        } catch (JWTVerificationException exception){
            throw new TokenVerificationException("Erro ao verificar o token de acesso");
        }
    }

    private Instant accessTokenExpiration() {
        return LocalDateTime.now().plusMinutes(20000).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant refreshTokenExpiration() {
        return LocalDateTime.now().plusMinutes(100000).toInstant(ZoneOffset.of("-03:00"));
    }

}
