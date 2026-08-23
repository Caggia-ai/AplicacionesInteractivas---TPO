package com.uade.tpo.demo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private int id;
    private String description;
}
