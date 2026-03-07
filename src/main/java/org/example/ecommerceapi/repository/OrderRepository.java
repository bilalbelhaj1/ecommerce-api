package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
    List<Order> findByCustomerId(Long id);
}
