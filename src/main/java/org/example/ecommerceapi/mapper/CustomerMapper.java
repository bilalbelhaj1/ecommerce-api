package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.entity.Customer;

/**
 * @author $(bilal belhaj)
 **/
public class CustomerMapper {

    public static Customer ToEntity(CreateCustomerDTO dto) {
        return  Customer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phoneNumber(dto.phoneNumber())
                .address(dto.address())
                .build();
    }

    public static CustomerResponseDTO toDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
    }

    public static CustomerSummaryDTO toSummary(Customer customer) {
        return new CustomerSummaryDTO(customer.getFirstName(), customer.getLastName());
    }
}
