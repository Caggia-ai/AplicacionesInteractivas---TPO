package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;

public interface CategoryService {
    public List<Category> getCategories();
    public Optional<Category> getCategoryById(Long categoryId);
    public Category createCategory(String description) throws CategoryDuplicateException;

}
