package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.dto.CategoryRequest;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    private CategoryService categoryService;

    public CategoriesController() {
        categoryService = new CategoryService();
    }

    @GetMapping
    public ResponseEntity<ArrayList<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getId(), categoryRequest.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }
}
