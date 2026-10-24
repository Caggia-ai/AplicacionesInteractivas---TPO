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

import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.uade.tpo.marketplace.entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll() // Para ver productos sin login
                                                .requestMatchers("/images/view/**").permitAll() // Para ver imágenes sin login
                                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/categories/**").hasAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PATCH, "/categories/**").hasAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAuthority(Role.ADMIN.name())   
                                                                                             // Vendedores y admins pueden gestionar productos
                                                .requestMatchers(HttpMethod.POST, "/products").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PATCH, "/products/**").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                // Cualquier otra petición requiere estar logueado (incluye carritos y compras)
                                                .anyRequest().authenticated()
                                        )
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                // Acá ponemos el puerto exacto donde corre tu frontend (Vite)
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                // Aplica esta configuración a todos los endpoints del backend
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
