package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.customer.CustomerResponseDTO;
import org.example.ecommerceapi.dto.customer.UpdateCustomerDTO;
import org.example.ecommerceapi.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Tag(name = "customers", description = "operations related to customers")
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // get customers
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    @Operation(
            summary = "get all customers",
            description = "admin endpoint to get all the customers"
    )
    public ResponseEntity<List<CustomerResponseDTO>> getCustomers() {
        return ResponseEntity.ok().body(customerService.getCustomers());
    }

    // get
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("{id}")
    @Operation(
            summary = "get customer data",
            description = "returns customer information's"
    )
    public ResponseEntity<CustomerResponseDTO> getCustomer(@Parameter(description = "Customer Id") @PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.getProfile(id));
    }

    // Update
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("{id}")
    @Operation(
            summary = "update customer ",
            description = "customer endpoint to update profile"
    )
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@Parameter(description = "Customer Id") @PathVariable Long id, @Valid @RequestBody UpdateCustomerDTO dto) {
        return ResponseEntity.ok().body(customerService.updateProfile(id, dto));
    }

}
