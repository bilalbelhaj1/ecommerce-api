package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * @author $(bilal belhaj)
 **/
@Tag(name = "Orders", description = "Operations related to orders (customer)")
@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    // place order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    @Operation(
            summary = "place Order",
            description = "place a new order"
    )
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderDTO dto) {
        URI uri = URI.create("http://localhost:8081/api/v1/orders");
        return ResponseEntity.created(uri).body(orderManagementService.placeOrder(dto));
    }

    // get all orders (customer)
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/{id}")
    @Operation(
            summary = "get orders",
            description = "returns all customer orders with pagination support and filtering by status"
    )
    public ResponseEntity<Page<OrderSummaryDTO>> getByCustomer(
            @Parameter(description = "Customer Id")  @PathVariable Long id,
            @Parameter(description = "order status") @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(orderManagementService.getCustomerOrders(id, status, page, size));
    }

    // get one order
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("{id}")
    @Operation(
            summary = "get order",
            description = "return order details"
    )
    public ResponseEntity<OrderResponseDTO> getById(
            @Parameter(description = "Order Id") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getOrder(id));
    }

    // cancel order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("{id}/cancel")
    @Operation(
            summary = "cancel order",
            description = "cancel order by customer"
    )
    public ResponseEntity<OrderSummaryDTO> cancelOrder(
            @Parameter(description = "Order Id") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.cancelOrder(id));
    }
}
