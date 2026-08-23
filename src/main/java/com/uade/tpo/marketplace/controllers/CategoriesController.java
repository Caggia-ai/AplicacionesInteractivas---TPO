package com.uade.tpo.marketplace.controllers;

import com.uade.tpo.marketplace.service.CategoryService;

import java.util.ArrayList;

import com.uade.tpo.marketplace.entity.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("categories")
public class CategoriesController {
    

    @GetMapping
    public ArrayList<Category> getCategories(){
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public String getCategoryById(@PathVariable int categoryId){
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public int createCategory(@RequestBody int categoryId) {  
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoryId);
    }
    
}
