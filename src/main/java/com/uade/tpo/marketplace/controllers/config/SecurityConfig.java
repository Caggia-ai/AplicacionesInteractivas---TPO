package com.uade.tpo.marketplace.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uade.tpo.marketplace.entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll() // Para ver productos sin login
                                                .requestMatchers("/images/view/**").permitAll() // Para ver imágenes sin login
                                                .requestMatchers(HttpMethod.POST, "/categories/**").hasAuthority(Role.ADMIN.name())// Solo admins pueden crear categorías
                                                // Vendedores y admins pueden gestionar productos
                                                .requestMatchers(HttpMethod.POST, "/products").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PATCH, "/products/**").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                
                                                // Cualquier otra petición requiere estar logueado (incluye carritos y compras)
                                                .anyRequest().authenticated()
                                        )
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
