package com.uade.tpo.marketplace.entity.dto;
import lombok.Data;

@Data
public class ProductPatchRequest {
    private String name;
    private String description;
    private Integer price; // Usamos Integer en vez de int para permitir nulls
    private Integer stock;
    private Integer discount_percentage;
}
