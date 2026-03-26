package org.example.ecommerceapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.orderManagement.*;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Order;
import org.example.ecommerceapi.entity.OrderItem;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.mapper.CustomerMapper;
import org.example.ecommerceapi.mapper.OrderManagementMapper;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.OrderRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.NotificationService;
import org.example.ecommerceapi.service.OrderManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class OrderManagementServiceImpl implements OrderManagementService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private NotificationService notificationService;

    // place order
    public OrderResponseDTO placeOrder(CreateOrderDTO dto) {
        // find customer
        Customer customer = customerRepository.findById(dto.customerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not found")
        );
        // create order
        Order order = OrderManagementMapper.toEntity(dto, customer);
        BigDecimal totalAmount = BigDecimal.ZERO;
        // create items
        for (CreateItemDTO itemDTO : dto.items()) {

            if(dto.items().isEmpty()){
                throw new BadRequestException("Order must contain at least one item");
            }

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

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // notify Customer
        notificationService.send(new CreateNotificationDTO(
                "ORDER_PLACED",
                "your order is placed successfully navigate to orders to keep track of your order",
                customer.getUser().getId()
        ));

        return new OrderResponseDTO(
                OrderManagementMapper.toSummaryDTO(order),
                order.getItems().stream()
                        .map(item -> OrderManagementMapper.toOrderItemDTO(item, item.getProduct()))
                        .toList(),
                CustomerMapper.toSummary(order.getCustomer())
        );
    }

    // view orders (all by admin)
    public Page<OrderSummaryDTO> getAllOrders(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = null;
        if (status != null) {
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        return orders
                .map(OrderManagementMapper::toSummaryDTO);
    }

    // view customer orders
    public Page<OrderSummaryDTO> getCustomerOrders(Long id, OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = null;
        if (status != null) {
            orders = orderRepository.findByCustomerIdAndStatus(id, status, pageable);
        } else {
            orders = orderRepository.findByCustomerId(id, pageable);
        }
        return orders
                .map(OrderManagementMapper::toSummaryDTO);
    }

    // view order by id
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );
        List<OrderItemResponse> itemsRes = order.getItems().stream()
                .map(item -> OrderManagementMapper.toOrderItemDTO(item, item.getProduct()))
                .toList();
        return new OrderResponseDTO(OrderManagementMapper.toSummaryDTO(order), itemsRes, CustomerMapper.toSummary(order.getCustomer()));
    }

    // cancel order
    public OrderSummaryDTO cancelOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        if(order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new BadRequestException("Cannot cancel delevered orders");
        }
        order.setStatus(OrderStatus.CANCELLED);

        return OrderManagementMapper.toSummaryDTO(orderRepository.save(order));
    }

    // update status
    public OrderSummaryDTO updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );

        order.setStatus(status);
        // notify Customer
        notificationService.send(new CreateNotificationDTO(
                "ORDER_" + status,
                "your order is " + status + " successfully navigate to orders to keep track of your order status",
                order.getCustomer().getUser().getId()
        ));
        return OrderManagementMapper.toSummaryDTO(orderRepository.save(order));
    }
}
