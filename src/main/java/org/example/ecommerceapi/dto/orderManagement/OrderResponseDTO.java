package org.example.ecommerceapi.dto.orderManagement;


import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public record OrderResponseDTO(
        OrderSummaryDTO orderSummary,
        List<OrderItemResponse> items
) {
}
