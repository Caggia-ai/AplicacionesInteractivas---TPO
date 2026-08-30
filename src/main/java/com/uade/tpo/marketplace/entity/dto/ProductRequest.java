package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private int price;
    private int stock;
    private int discount_percentage;
    private Long id_category;
    private Long id_user;
}