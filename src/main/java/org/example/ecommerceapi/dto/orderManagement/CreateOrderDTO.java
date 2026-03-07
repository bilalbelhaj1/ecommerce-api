package org.example.ecommerceapi.dto.orderManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public record CreateOrderDTO(
        Long customerId,
        List<CreateItemDTO> items,
        @NotBlank @Size(min = 3)
        String shippingAddress
) {
}
