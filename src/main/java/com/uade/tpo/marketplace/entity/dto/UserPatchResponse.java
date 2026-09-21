package com.uade.tpo.marketplace.entity.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchResponse {
    private UserResponse user;
    private String accessToken;
}
