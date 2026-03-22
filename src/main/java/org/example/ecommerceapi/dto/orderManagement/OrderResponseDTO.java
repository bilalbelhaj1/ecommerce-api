package org.example.ecommerceapi.dto.orderManagement;


import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public record OrderResponseDTO(
        OrderSummaryDTO orderSummary,
        List<OrderItemResponse> items,
        CustomerSummaryDTO customer
) {
}
