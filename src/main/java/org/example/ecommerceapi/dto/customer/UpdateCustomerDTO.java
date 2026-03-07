package org.example.ecommerceapi.dto.customer;

/**
 * @author $(bilal belhaj)
 **/
public record UpdateCustomerDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address
) {
}
