package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long> {
}
