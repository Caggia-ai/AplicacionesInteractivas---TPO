package com.uade.tpo.marketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param; // IMPORTANTE AGREGAR ESTO

import com.uade.tpo.marketplace.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p where p.name = ?1")
    Optional<Product> findByName(String name);

    @Query(value = "select p from Product p where p.description = ?1")
    Optional<Product> findByDescription(String description);

    @Query(value = "select p from Product p where p.state = true")
    Page<Product> findByStateTrue(Pageable pageable);

    @Query(value = "select p from Product p where p.id_product = ?1 and p.state = true")
    Optional<Product> findByIdAndState(Long id, Boolean state);

    // FILTRADO DINÁMICO
    @Query("SELECT p FROM Product p WHERE p.state = true " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findWithFilters(@Param("categoryId") Long categoryId, 
                                  @Param("minPrice") Integer minPrice, 
                                  @Param("maxPrice") Integer maxPrice, 
                                  @Param("keyword") String keyword, 
                                  Pageable pageable);
}