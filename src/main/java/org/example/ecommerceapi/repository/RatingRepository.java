package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $(bilal belhaj)
 **/
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
