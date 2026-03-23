package com.fatec.labify.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.labify.api.dto.authentication.TokenData;
import com.fatec.labify.api.dto.authentication.UserTokenDTO;
import com.fatec.labify.client.GoogleClient;
import com.fatec.labify.domain.User;
import com.fatec.labify.domain.UserToken;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.TokenRepository;
import com.fatec.labify.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class SSOService {
    private ObjectMapper objectMapper;
    private final GoogleClient googleClient;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public SSOService(ObjectMapper objectMapper, GoogleClient googleClient, TokenRepository tokenRepository, UserRepository userRepository, TokenService tokenService) {
        this.objectMapper = objectMapper;
        this.googleClient = googleClient;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public TokenData exchangeCodeAndSaveToken(String code) throws JsonProcessingException {
        UserTokenDTO dto = googleClient.getUserToken(code);
        User user = getUser(dto.getIdToken());
        UserToken token = new UserToken(
                user.getId(),
                "google",
                dto.getAccessToken(),
                dto.getRefreshToken(),
                dto.getIdToken(),
                Instant.now().plusSeconds(dto.getExpiresIn())
        );

        tokenRepository.save(token);
        return new TokenData(tokenService.generateToken(user), tokenService.generateRefreshToken(user));
    }


    public User getUser(String idToken) throws JsonProcessingException {
        String email = getUsernameFromToken(idToken);
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("Usuário", email));
    }


    private String getUsernameFromToken(String idToken) throws JsonProcessingException {
        String[] parts = idToken.split("\\.");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        String payload = new String(Base64.getDecoder().decode(parts[1]));
        Map<String, Object> payloadToMap = objectMapper.readValue(payload, new TypeReference<>() {});

        String email = String.valueOf(payloadToMap.get("email"));

        return email;
    }


}
