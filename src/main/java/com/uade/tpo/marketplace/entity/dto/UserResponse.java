package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;

    // Método estático para convertir Entidad -> DTO
    public static UserResponse fromEntity(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId_user());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}