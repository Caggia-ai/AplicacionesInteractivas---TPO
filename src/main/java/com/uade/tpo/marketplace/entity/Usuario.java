package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Usuario {
    private int id_usuario;
    private String nombre_usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private String rol;
    private boolean estado;
}
