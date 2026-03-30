package org.example.ecommerceapi.repository;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository underTest;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Customer customer;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(
                Customer.builder()
                        .email("bilal@gmail.com")
                        .address("address")
                        .firstName("bilal")
                        .lastName("belhaj")
                        .appUser(
                                AppUser.builder()
                                        .username("bilal@gmail.com")
                                        .password("password")
                                        .build()
                        )
                        .build()
        );
        Category cat = categoryRepository.save(Category.builder().name("cat").description("des").build());
        product = productRepository.save(
                Product.builder()
                        .name("prod")
                        .description("des")
                        .status(ProductStatus.ACTIVE)
                        .stock(12)
                        .price(BigDecimal.TEN)
                        .category(cat)
                        .imageType("png")
                        .imageData(new byte[12])
                        .build()
        );
        order = Order.builder()
                .date(LocalDateTime.now())
                .customer(customer)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(BigDecimal.valueOf(100))
                .shippingAddress(customer.getAddress())
                .items(List.of(OrderItem.builder().product(product).quantity(2).build()))
                .build();
    }

    @Test
    void findByCustomerId_returnsOrders_whenExists() {
        // given
        underTest.save(order);
        // when
        Page<Order> res = underTest.findByCustomerId(customer.getId(), PageRequest.of(0, 10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void findByCustomerId_returnsEmptyList_whenNotExists() {
        // when
        Page<Order> res = underTest.findByCustomerId(customer.getId(), PageRequest.of(0, 10));
        // then
        assertThat(res.getContent()).isEmpty();
    }

    @Test
    void findByCustomerIdAndStatus_returnsOrders_whenExists() {
        // given
        Order saved = underTest.save(order);
        // when
        Page<Order> res = underTest.findByCustomerIdAndStatus(customer.getId(),saved.getStatus(),PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }
    @Test
    void findByCustomerIdAndStatus_returnsEmptyList_whenNotExists() {
        // when
        Page<Order> res = underTest.findByCustomerIdAndStatus(customer.getId(),OrderStatus.CONFIRMED,PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isEmpty();
    }

    @Test
    void findByStatus_returnsOrders_whenExists() {
        // given
        Order saved = underTest.save(order);
        // when
        Page<Order> res = underTest.findByStatus(saved.getStatus(),PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void findByStatus_returnsEmptyList_whenNotExists() {
        // when
        Page<Order> res = underTest.findByStatus(OrderStatus.CONFIRMED,PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isEmpty();
    }
}