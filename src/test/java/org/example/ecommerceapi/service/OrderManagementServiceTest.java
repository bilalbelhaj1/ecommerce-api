package org.example.ecommerceapi.service;

import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.OrderRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.impl.OrderManagementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class OrderManagementServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ProductRepository productRepository;
    @Mock private NotificationService notificationService;
    @InjectMocks private OrderManagementServiceImpl underTest;

    // test place order
    @Test
    void canPlaceOrder() {
    }

    @Test
    void PlaceOrder_Throws_whenCustomerNotExists() {
    }

    @Test
    void PlaceOrder_throws_whenNoItems() {
    }

    @Test
    void PlaceOrder_throws_ProductNotExists() {
    }

    @Test
    void PlaceOrder_throws_invalidStock() {
    }


    @Test
    void getAllOrders() {
    }

    @Test
    void getAllOrders_ByStatus() {
    }

    @Test
    void getCustomerOrders() {
    }

    @Test
    void getCustomerOrders_byStatus() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void getOrder_throws_whenNotExists() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void cancelOrder_throws_whenNotExists() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void updateStatus_throws_whenOrderNotExists() {
    }
}