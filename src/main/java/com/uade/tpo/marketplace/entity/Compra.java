package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Compra {
    private int id_compra;
    private String metodo_pago;
    private String fecha;
    private int total;
    private String forma_entrega;
    private String tiempo_envio;
    private String origen;
}
