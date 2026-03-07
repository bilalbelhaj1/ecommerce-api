package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
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
    public void createAccount(CreateCustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already exists");
        }
        String hash = passwordEncoder.encode(dto.password());
        Customer customer = Customer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .passwordHash(hash)
                .build();
        if (dto.address() != null && !dto.address().isEmpty()) {
            customer.setAddress(dto.address());
        }

        if (dto.phoneNumber() != null && !dto.phoneNumber().isEmpty()) {
            customer.setPhoneNumber(dto.phoneNumber());
        }
        customerRepository.save(customer);
    }
    // get profile
    public CustomerResponseDTO getProfile(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
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

        Customer saved = customerRepository.save(customer);
        return new CustomerResponseDTO(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.getAddress()
        );
    }

    // delete account
    public void deleteAccount(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
    // login

    // reset password
}
