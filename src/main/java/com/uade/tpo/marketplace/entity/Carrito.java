package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Carrito {
    private int id_carrito;
    private Usuario usuario;
    private int total;
    private String vencimiento;
    private boolean estado;
}
