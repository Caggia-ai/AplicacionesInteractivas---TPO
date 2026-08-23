package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository {
    private ArrayList<Category> categories;

    public CategoryRepository() {
        categories = new ArrayList<Category>(
                Arrays.asList(Category.builder().description("Figuras y Coleccionables").id(1).build(),
                        Category.builder().description("Peluches").id(2).build(),
                        Category.builder().description("Indumentaria").id(3).build(),
                        Category.builder().description("Libros y Manga").id(4).build(),
                        Category.builder().description("Juegos y cartas").id(5).build(),
                        Category.builder().description("Cosplay").id(6).build()));
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public Optional<Category> getCategoryById(int categoryId) {
        return this.categories.stream().filter(m -> m.getId() == categoryId).findAny();
    }

    public Category createCategory(int newCategoryId, String description) {
        Category newCategory = Category.builder()
                .description(description)
                .id(newCategoryId).build();
        this.categories.add(newCategory);
        return newCategory;
    }
}
