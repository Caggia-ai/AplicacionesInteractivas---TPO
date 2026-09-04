package com.uade.tpo.marketplace.entity.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    // Opcional: si no se manda, AuthenticationService lo completa con "USER".
    private String role;
}
