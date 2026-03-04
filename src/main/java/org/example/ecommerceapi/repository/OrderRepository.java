package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
}
