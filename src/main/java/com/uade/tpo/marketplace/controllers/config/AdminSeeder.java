package com.uade.tpo.marketplace.controllers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.Role;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AdminSeeder {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.admin.email:}")
    private String adminEmail;

    @Value("${application.security.admin.password:}")
    private String adminPassword;

    // Se ejecuta una sola vez al levantar la app.
    // Si ya existe un usuario con ese email, no hace nada (evita duplicados
    // en reinicios cuando ddl-auto no sea create-drop).
    @Bean
    public CommandLineRunner seedAdmin() {
        return args -> {
            if (adminEmail.isBlank() || adminPassword.isBlank()) {
                return; // no configuraron las variables, no seedeamos nada
            }
            if (userRepository.findByEmail(adminEmail).isPresent()) {
                return; // ya existe, no lo recreamos
            }

            User admin = new User(
                    "admin",
                    "Admin",
                    "Admin",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN
            );
            User saved = userRepository.save(admin);

            Cart cart = new Cart();
            cart.setUser(saved);
            cart.setState(true);
            cartRepository.save(cart);
        };
    }
}
