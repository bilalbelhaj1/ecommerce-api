package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.example.ecommerceapi.dto.customer.CreateCustomerDTO;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * @author $(bilal belhaj)
 **/

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    // create customer
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CreateCustomerDTO dto) {
        CustomerResponseDTO res = customerService.createAccount(dto);
        URI uri = URI.create("http://localhost:8080/api/v1/customer/" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // get
    @GetMapping("{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.getProfile(id));
    }

    // Update
    @PutMapping("{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerDTO dto) {
        return ResponseEntity.ok().body(customerService.updateProfile(id, dto));
    }

}
