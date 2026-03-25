package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface CustomerService {
    List<CustomerResponseDTO> getCustomers();
    CustomerResponseDTO getProfile(Long id);
    CustomerResponseDTO updateProfile(Long id, UpdateCustomerDTO dto);
    void deleteAccount(Long id);
}
