package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
    int countByCategoryId(Long id);
    boolean existsByName(String name);
    @Query(
            "SELECT avg(rating) FROM Rating WHERE product = Product"
    )
    double rating();

    @Query(
            "SELECT count(*) FROM Rating WHERE product = Product "
    )
    int nbrRatings();
}
