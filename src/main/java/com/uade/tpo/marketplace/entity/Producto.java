package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Producto {
    private int id_producto;
    private Category category;
    private Usuario usuario;
    private String nombre;
    private String descripcion;
    private int precio;
    private int stock;
    private int porcentaje_descuento;
    private boolean estado;
    
}
