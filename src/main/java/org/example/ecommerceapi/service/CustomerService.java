package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.entity.AppUser;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface CustomerService {
    List<CustomerResponseDTO> getCustomers();
    CustomerResponseDTO getProfile(Long id);
    CustomerResponseDTO updateProfile(Long id, UpdateCustomerDTO dto);
    CustomerSummaryDTO create(CreateCustomerDTO dto, AppUser user);
    void deleteAccount(Long id);
}
