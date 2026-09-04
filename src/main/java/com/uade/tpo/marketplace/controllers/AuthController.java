package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.entity.dto.auth.RegisterRequest;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.service.AuthenticationService;

// Antes no existía ningún controller para /api/v1/auth/**: SecurityConfig lo dejaba
// pasar libre (permitAll) pero no había forma real de registrarse ni loguearse,
// así que ningún otro endpoint (todos exigen JWT) era alcanzable.
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
            throws UserDuplicateException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
