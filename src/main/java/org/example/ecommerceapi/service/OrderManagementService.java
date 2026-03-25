package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.orderManagement.CreateOrderDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.enums.OrderStatus;
import org.springframework.data.domain.Page;

/**
 * @author $(bilal belhaj)
 **/
public interface OrderManagementService {
    OrderResponseDTO placeOrder(CreateOrderDTO dto);
    Page<OrderSummaryDTO> getAllOrders(OrderStatus status, int page, int size);
    Page<OrderSummaryDTO> getCustomerOrders(Long id, OrderStatus status, int page, int size);
    OrderResponseDTO getOrder(Long id);
    OrderSummaryDTO cancelOrder(Long id);
    OrderSummaryDTO updateStatus(Long id, OrderStatus status);
}
