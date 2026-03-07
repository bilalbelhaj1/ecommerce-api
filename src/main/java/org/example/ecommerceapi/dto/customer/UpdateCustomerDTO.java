package org.example.ecommerceapi.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * @author $(bilal belhaj)
 **/
public record UpdateCustomerDTO(
        @Size(min = 3, max = 100)
        String firstName,

        @Size(min = 3, max = 100)
        String lastName,

        @Email
        String email,

        @Size(min = 9, max = 15)
        String phoneNumber,
        
        @Size(min = 3, max = 255)
        String address
) {
}
