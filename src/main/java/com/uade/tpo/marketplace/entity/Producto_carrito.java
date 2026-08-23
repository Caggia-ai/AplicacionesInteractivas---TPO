package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Producto_carrito {
    private int id_producto_carrito;
    private Carrito carrito;
    private Producto producto;
    private int precio_unitario;
    private int cantidad;
}
