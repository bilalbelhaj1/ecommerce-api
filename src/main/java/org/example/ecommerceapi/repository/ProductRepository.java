package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
    int countByCategoryId(Long id);
    boolean existsByName(String name);

    @Query("SELECT COALESCE(avg(r.rating), 0) FROM Rating r WHERE r.product.id = :id")
    double rating(@Param("id") Long id);

    @Query("SELECT count(r) FROM Rating r WHERE r.product = :product")
    int nbrRatings(@Param("product") Product product);

    Page<Product> findByStatusAndCategory(ProductStatus status, Category category, Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :q ,'%')) OR "+
            "LOWER(CAST(p.description as string ) ) LIKE LOWER(CONCAT('%', :q ,'%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :q ,'%')) "
    )
    Page<Product> searchProducts(String q, Pageable pageable);
}
