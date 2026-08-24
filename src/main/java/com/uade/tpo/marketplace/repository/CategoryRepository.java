package com.uade.tpo.marketplace.repository;
import com.uade.tpo.marketplace.entity.Category;

// import java.util.ArrayList;


// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository <Category, Long> {

    // public ArrayList<Category> getCategories();
    // public Optional<Category> getCategoryById(Long categoryId);
    // public Category createCategory( String description);

}
