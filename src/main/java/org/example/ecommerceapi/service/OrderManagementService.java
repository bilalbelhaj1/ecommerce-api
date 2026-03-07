package org.example.ecommerceapi.service;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.orderManagement.*;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Order;
import org.example.ecommerceapi.entity.OrderItem;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.OrderItemRepository;
import org.example.ecommerceapi.repository.OrderRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
public class OrderManagementService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderManagementService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // place order
    public OrderResponseDTO placeOrder(CreateOrderDTO dto) {
        // find customers
        Customer customer = customerRepository.findById(dto.customerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );
        // create order
        Order order = Order.builder()
                .shippingAddress(dto.shippingAddress())
                .date(LocalDateTime.now())
                .customer(customer)
                .status(OrderStatus.PROCESSING)
                .build();
        BigDecimal totalAmount = BigDecimal.ZERO;
        // create items
        for (CreateItemDTO itemDTO : dto.items()) {
            // check if product exists then create item
            Product product = productRepository.findById(itemDTO.productId()).orElseThrow(
                    () -> new ResourceNotFoundException("Product not found ")
            );
            if (product.getStock() < itemDTO.quantity()) {
                throw new BadRequestException("Not enough stock for product " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .quantity(itemDTO.quantity())
                    .unitPrice(product.getPrice())
                    .product(product)
                    .order(order)
                    .build();

            order.addItem(orderItem);
            // update stock
            product.setStock(product.getStock() - itemDTO.quantity());
            productRepository.save(product);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return new OrderResponseDTO(
                new OrderSummaryDTO(order.getDate(), order.getShippingAddress(), order.getTotalAmount(), order.getStatus()),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getProduct().getName(),
                                item.getProduct().getId()
                        ))
                        .toList()
        );
    }

    // view orders (all by admin)
    public List<OrderSummaryDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(or-> new OrderSummaryDTO(
                        or.getDate(),
                        or.getShippingAddress(),
                        or.getTotalAmount(),
                        or.getStatus()
                ))
                .toList();
    }
    // view customer orders
    public List<OrderSummaryDTO> getCustomerOrders(Long id) {
        return orderRepository.findByCustomerId(id).stream()
                .map(or -> new OrderSummaryDTO(
                        or.getDate(),
                        or.getShippingAddress(),
                        or.getTotalAmount(),
                        or.getStatus()
                ))
                .toList();
    }
    // view order by id
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );
        return new OrderResponseDTO(new OrderSummaryDTO(
                order.getDate(),
                order.getShippingAddress(),
                order.getTotalAmount(),
                order.getStatus()
        ), order.getItems().stream().map(item -> new OrderItemResponse(
                item.getUnitPrice(),
                item.getQuantity(),
                item.getProduct().getName(),
                item.getProduct().getId()
        )).toList());
    }
    // cancel order

    // view items

    // update status
}
