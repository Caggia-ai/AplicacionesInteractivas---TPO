package com.uade.tpo.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.Product;


//@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p where p.name = ?1")
    Optional<Product> findByName(String name);

    @Query(value = "select p from Product p where p.description = ?1")
    Optional<Product> findByDescription(String description);

    @Query(value = "select p from Product p where p.state = true")
    Page<Product> findByStateTrue(Pageable pageable);

    @Query(value = "select p from Product p where p.id = ?1 and p.state = true")
    Optional<Product> findByIdAndState(Long id, Boolean state);
}