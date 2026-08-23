package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Detalle_Compra {
    private int id_detalle_compra;
    private Compra compra;
    private Producto producto;
    private int precio_unitario;
    private int cantidad;
}