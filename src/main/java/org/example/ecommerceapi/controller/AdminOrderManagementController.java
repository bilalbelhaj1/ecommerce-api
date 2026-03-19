package org.example.ecommerceapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.service.OrderManagementService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author $(bilal belhaj)
 **/
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderManagementController {
    private final OrderManagementService orderManagementService;

    // get orders
    @GetMapping
    public ResponseEntity<Page<OrderSummaryDTO>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(orderManagementService.getAllOrders(status, page, size));
    }

    // get one order
    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getOrder(id));
    }

    // get orders by customer
    @GetMapping("/customer/{id}")
    public ResponseEntity<Page<OrderSummaryDTO>> getCustomerOrders(
            @PathVariable Long id,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok().body(orderManagementService.getCustomerOrders(id, status, page, size));
    }

    // update status
    @PutMapping("{id}")
    public ResponseEntity<OrderSummaryDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok().body(orderManagementService.updateStatus(id, status));
    }
}
