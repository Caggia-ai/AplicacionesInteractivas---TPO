package com.uade.tpo.marketplace.controllers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uade.tpo.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

// Esta clase NO decide nada: solo le pasa los valores de configuración
// al service, que es quien tiene la lógica de negocio de "crear el admin
// si hace falta".
@Configuration
@RequiredArgsConstructor
public class AdminSeeder {

    private final UserService userService;

    @Value("${application.security.admin.email:}")
    private String adminEmail;

    @Value("${application.security.admin.password:}")
    private String adminPassword;

    @Bean
    public CommandLineRunner seedAdmin() {
        return args -> userService.seedAdminIfNotExists(adminEmail, adminPassword);
    }
}
