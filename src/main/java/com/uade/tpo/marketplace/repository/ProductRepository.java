package com.uade.tpo.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Product;

//@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p where p.name = ?1")
    Optional<Product> findByName(String name);

    @Query(value = "select p from Product p where p.description = ?1")
    Optional<Product> findByDescription(String description);
}