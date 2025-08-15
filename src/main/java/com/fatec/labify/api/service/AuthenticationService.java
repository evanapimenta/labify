package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.authentication.TokenData;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.UserNotVerifiedException;
import com.fatec.labify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public TokenData login(User user) {
        validateVerified(user);

        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        user.updateLastLogin();
        userRepository.save(user);
        return new TokenData(accessToken, refreshToken);
    }

    @Transactional
    public TokenData requestRefreshToken(String refreshToken) {
        User user = tokenService.validateAndGetUserFromToken(refreshToken);

        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        user.updateLastLogin();
        userRepository.save(user);
        return new TokenData(newAccessToken, newRefreshToken);
    }

    public void validateVerified(User user) {
        if (!user.isVerified()) {
            throw new UserNotVerifiedException();
        }
    }

}