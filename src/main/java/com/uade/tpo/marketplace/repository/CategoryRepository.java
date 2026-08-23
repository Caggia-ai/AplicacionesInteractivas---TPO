package com.uade.tpo.marketplace.repository;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.tpo.marketplace.entity.Category;


public class CategoryRepository {
    public ArrayList<Category> categories= new ArrayList<Category>(
        Arrays.asList(Category.builder().id(1).description("Electronica").build(),
                    Category.builder().id(2).description("Sillas").build(),
                    Category.builder().id(3).description("Colchones").build())
    );

    public ArrayList<Category> getCategories(){
        return this.categories;
    }

    public String getCategoryById(@PathVariable int categoryId){
        return null;
    }

    public String createCategory(@RequestBody int entity) {  
        //metodo      
        return null;
    }
}
