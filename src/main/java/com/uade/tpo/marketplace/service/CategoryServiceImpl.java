package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category createCategory(String description) throws CategoryDuplicateException {
        Optional<Category> categories = categoryRepository.findByDescription(description);
        if (categories.isEmpty())
            return categoryRepository.save(new Category(description));
        throw new CategoryDuplicateException();
    }

    public Category updateCategory(Long categoryId, String newDescription) throws CategoryDuplicateException {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Verificamos si el nuevo nombre ya lo usa otra categoría
        Optional<Category> existingCategory = categoryRepository.findByDescription(newDescription);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryId)) {
            throw new CategoryDuplicateException();
        }

        category.setDescription(newDescription);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Validación crítica: evitar borrar una categoría que ya está siendo usada por productos
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene productos asociados.");
        }

        categoryRepository.delete(category);
    }
}
