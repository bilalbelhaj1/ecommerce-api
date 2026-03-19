package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Order;
import org.example.ecommerceapi.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
    Page<Order> findByCustomerId(Long id, Pageable pageable);
    Page<Order> findByCustomerIdAndStatus(Long id, OrderStatus status, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
