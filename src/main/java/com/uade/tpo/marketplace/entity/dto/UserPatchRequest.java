package com.uade.tpo.marketplace.entity.dto;
import lombok.Data;

@Data
public class UserPatchRequest{
  private String username;
  private String name; 
  private String surname;
  private String email;
  private String password;
  private String role;
}
