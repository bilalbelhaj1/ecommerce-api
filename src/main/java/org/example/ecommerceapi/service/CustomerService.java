package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author $(bilal belhaj)
 **/
@Transactional
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // create account
    public CustomerResponseDTO createAccount(CreateCustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already exists");
        }
        Customer customer = CustomerMapper.ToEntity(dto);
        String hash = passwordEncoder.encode(dto.password());
        customer.setPasswordHash(hash);
        return CustomerMapper.toDTO(customerRepository.save(customer));
    }

    // get profile
    public CustomerResponseDTO getProfile(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );
        return CustomerMapper.toDTO(customer);
    }

    // update account
    public CustomerResponseDTO updateProfile(Long id, UpdateCustomerDTO dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );

        if (dto.email() != null && !dto.email().isEmpty() && !dto.email().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(dto.email())) {
                throw new BadRequestException("Email already exists");
            }
            customer.setEmail(dto.email());
        }
        if (dto.firstName() != null && !dto.firstName().isEmpty()) {
            customer.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null && !dto.lastName().isEmpty()) {
            customer.setLastName(dto.lastName());
        }
        if (dto.phoneNumber() != null && !dto.phoneNumber().isEmpty()) {
            customer.setPhoneNumber(dto.phoneNumber());
        }
        if (dto.address() != null && !dto.address().isEmpty()) {
            customer.setAddress(dto.address());
        }

        return CustomerMapper.toDTO(customerRepository.save(customer));
    }

    // delete account
    public void deleteAccount(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
