package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String description;

    public static CategoryResponse fromEntity(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setDescription(category.getDescription());
        return dto;
    }
}