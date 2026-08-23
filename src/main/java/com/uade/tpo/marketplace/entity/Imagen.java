package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Imagen {
    private int id_imagen;
    private String url;
}
