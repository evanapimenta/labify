package com.fatec.labify.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatec.labify.api.dto.authentication.LoginDataDTO;
import com.fatec.labify.api.dto.authentication.RefreshTokenData;
import com.fatec.labify.api.dto.authentication.TokenData;
import com.fatec.labify.api.service.AuthenticationService;
import com.fatec.labify.api.service.SSOService;
import com.fatec.labify.client.GoogleClient;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.BaseException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Hidden
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final GoogleClient googleClient;
    private final AuthenticationManager authenticationManager;
    private final SSOService ssoService;

    public AuthenticationController(AuthenticationService authenticationService, GoogleClient googleClient, AuthenticationManager authenticationManager, SSOService ssoService) {
        this.authenticationService = authenticationService;
        this.googleClient = googleClient;
        this.authenticationManager = authenticationManager;
        this.ssoService = ssoService;
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
        return ResponseEntity.ok(newTokenData);
    }

    @GetMapping("login/google")
    public ResponseEntity<Void> loginWithGoogle() {
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, googleClient.buildAuthorizationUrl()).build();
    }

    @GetMapping("login/google/callback")
    public ResponseEntity<TokenData> callback(@RequestParam("code") String code) throws JsonProcessingException {
        TokenData tokenData = ssoService.exchangeCodeAndSaveToken(code);
        return ResponseEntity.ok(tokenData);
    }

}
