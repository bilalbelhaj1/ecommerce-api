package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByProduct(Product product);
}
