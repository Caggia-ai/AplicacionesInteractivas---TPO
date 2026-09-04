package com.uade.tpo.marketplace.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.entity.dto.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.entity.dto.auth.RegisterRequest;
import com.uade.tpo.marketplace.controllers.config.JwtService;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UserDuplicateException {
        String role = (request.getRole() != null && !request.getRole().isBlank())
                ? request.getRole()
                : "USER";

        // userService.createUser ya valida username duplicado, encripta la contraseña
        // con BCrypt y crea el carrito inicial del usuario.
        User user = userService.createUser(
                request.getUsername(),
                request.getName(),
                request.getSurname(),
                request.getEmail(),
                request.getPassword(),
                role);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
