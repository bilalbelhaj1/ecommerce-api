package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Orders-management-admin", description = "operations related to orders (admin)")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderManagementController {
    private final OrderManagementService orderManagementService;

    // get orders
    @GetMapping
    @Operation(
            summary = "get orders",
            description = "returns all orders with pagination and filtering"
    )
    public ResponseEntity<Page<OrderSummaryDTO>> getOrders(
            @Parameter(description = "Order Status") @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(orderManagementService.getAllOrders(status, page, size));
    }

    // get one order
    @GetMapping("{id}")
    @Operation(
            summary = "get order",
            description = "return order details"
    )
    public ResponseEntity<OrderResponseDTO> getOrder(
            @Parameter(description = "Order Id") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getOrder(id));
    }

    // get orders by customer
    @GetMapping("/customer/{id}")
    @Operation(
            summary = "get customer orders",
            description = "returns all orders of a specific customer"
    )
    public ResponseEntity<Page<OrderSummaryDTO>> getCustomerOrders(
            @Parameter(description = "Customer Id") @PathVariable Long id,
            @Parameter(description = "Order status") @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok().body(orderManagementService.getCustomerOrders(id, status, page, size));
    }

    // update status
    @PutMapping("{id}")
    @Operation(
            summary = "update order status",
            description = "update the order status"
    )
    public ResponseEntity<OrderSummaryDTO> updateStatus(
            @Parameter(description = "Order Id") @PathVariable Long id,
            @Parameter(description = "new Order Status") @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok().body(orderManagementService.updateStatus(id, status));
    }
}
