package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.example.ecommerceapi.dto.orderManagement.CreateOrderDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.service.OrderManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping("api/v1/orders")
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    // place order
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderDTO dto) {
        URI uri = URI.create("http://localhost:8081/api/v1/orders");
        return ResponseEntity.created(uri).body(orderManagementService.placeOrder(dto));
    }
    // get all orders (admin)
    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getAll() {
        return ResponseEntity.ok().body(orderManagementService.getAllOrders());
    }

    // get all orders (customer)
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderSummaryDTO>> getByCustomer(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getCustomerOrders(id));
    }

    // get one order
    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.getOrder(id));
    }

    // get by status
    @GetMapping("/status")
    public ResponseEntity<List<OrderSummaryDTO>> getByStatus(
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok().body(orderManagementService.getByStatus(status));
    }

    // cancel order
    @PutMapping("{id}")
    public ResponseEntity<OrderSummaryDTO> cancelOrder(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(orderManagementService.cancelOrder(id));
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
