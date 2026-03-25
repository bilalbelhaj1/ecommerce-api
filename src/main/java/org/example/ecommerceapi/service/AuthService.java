package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.auth.LoginDTO;
import org.example.ecommerceapi.dto.auth.LoginResponseDTO;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;

/**
 * @author $(bilal belhaj)
 **/
public interface AuthService {
    CustomerSummaryDTO createAccount(CreateCustomerDTO dto);
    LoginResponseDTO login(LoginDTO dto);
}
