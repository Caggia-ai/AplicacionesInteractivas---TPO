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
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository repository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UserDuplicateException {
        String requestedRole = (request.getRole() != null && !request.getRole().isBlank()) 
                ? request.getRole().toUpperCase() 
                : "BUYER";
                
        // Bloqueo de seguridad: si intentan registrarse como ADMIN desde afuera, se fuerza el rol básico.
        if (requestedRole.equals("ADMIN")) {
                requestedRole = "BUYER"; 
        }

        User user = userService.createUser(
                request.getUsername(),
                request.getName(),
                request.getSurname(),
                request.getEmail(),
                request.getPassword(),
                requestedRole);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Autenticamos usando el dato que venga en el JSON (puede ser email o username)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Buscamos al usuario por email o por username
        User user = repository.findByEmail(request.getEmail())
                .or(() -> repository.findByUsername(request.getEmail()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}