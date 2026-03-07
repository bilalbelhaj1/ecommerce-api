package org.example.ecommerceapi.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author $(bilal belhaj)
 **/
public record CreateCustomerDTO(
        @NotBlank @Size(min = 3, max = 100)
        String firstName,

        @NotBlank @Size(min = 3, max = 100)
        String lastName,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8)
        String password,

        String address,
        String phoneNumber
) {
}
