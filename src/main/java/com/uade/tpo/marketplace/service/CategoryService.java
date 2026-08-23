package com.uade.tpo.marketplace.service;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.entity.*;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("categories")
public class CategoryService {
    
    public ArrayList<Category> getCategories(){
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategories();
    }
    

    public String getCategoryById(@PathVariable int categoryId){
        return new String();
    }

    public int createCategory(@RequestBody int categoryId) {  
        //metodo      
        return categoryId;
    }
    
}
