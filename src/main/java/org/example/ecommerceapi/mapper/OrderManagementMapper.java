package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.orderManagement.OrderItemResponse;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.entity.Order;
import org.example.ecommerceapi.entity.OrderItem;
import org.example.ecommerceapi.entity.Product;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public final class OrderManagementMapper {
    public static OrderSummaryDTO toSummaryDTO(Order order) {
        return new OrderSummaryDTO(
                order.getDate(),
                order.getShippingAddress(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    public static OrderItemResponse toOrderItemDTO(OrderItem item, Product product) {
        return new OrderItemResponse(
                item.getUnitPrice(),
                item.getQuantity(),
                product.getName(),
                product.getId()
        );
    }
}
