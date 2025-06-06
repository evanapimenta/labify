package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.authentication.LoginDataDTO;
import com.fatec.labify.api.dto.authentication.RefreshTokenData;
import com.fatec.labify.api.dto.authentication.TokenData;
import com.fatec.labify.api.service.AuthenticationService;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.BaseException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenData> login(@Valid @RequestBody LoginDataDTO loginData) throws BaseException {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginData.email(), loginData.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        TokenData tokenData = authenticationService.login(user);
        return ResponseEntity.ok(tokenData);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenData> refreshToken(@Valid @RequestBody RefreshTokenData tokenData) {
        String refreshToken = tokenData.refreshToken();

        TokenData newTokenData = authenticationService.requestRefreshToken(refreshToken);
        return ResponseEntity.ok(newTokenData);    }
}
