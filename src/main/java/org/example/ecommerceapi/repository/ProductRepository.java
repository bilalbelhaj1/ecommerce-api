package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
    public int countByCategoryId(Long id);
    boolean existsByName(String name);
}
