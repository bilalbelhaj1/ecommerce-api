package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.orderManagement.CreateOrderDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.service.OrderManagementService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    // place order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderDTO dto) {
        URI uri = URI.create("http://localhost:8081/api/v1/orders");
        return ResponseEntity.created(uri).body(orderManagementService.placeOrder(dto));
    }

    // get all orders (customer)
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/{id}")
    public ResponseEntity<Page<OrderSummaryDTO>> getByCustomer(
            @PathVariable Long id,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(orderManagementService.getCustomerOrders(id, status, page, size));
    }

    // get one order
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getOrder(id));
    }

    // cancel order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("{id}/cancel")
    public ResponseEntity<OrderSummaryDTO> cancelOrder(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.cancelOrder(id));
    }

//    // update status
//    @PutMapping("/status/{id}")
//    public ResponseEntity<OrderSummaryDTO> updateStatus(
//            @PathVariable Long id,
//            @RequestParam OrderStatus status
//            ) {
//        return ResponseEntity.ok().body(orderManagementService.updateStatus(id, status));
//    }

}
