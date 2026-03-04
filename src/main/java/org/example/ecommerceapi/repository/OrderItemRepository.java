package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author $(bilal belhaj)
 **/

@Repository
public interface OrderItemRepository extends JpaRepository <OrderItem, Long> {
}
