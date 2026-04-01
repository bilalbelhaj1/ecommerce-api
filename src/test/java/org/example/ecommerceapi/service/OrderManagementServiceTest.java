package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.orderManagement.CreateItemDTO;
import org.example.ecommerceapi.dto.orderManagement.CreateOrderDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.OrderRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.impl.OrderManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private Order order;
    private Product product;
    private Customer customer;


    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("prod")
                .description("desc")
                .price(BigDecimal.TEN)
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .imageData(new byte[12])
                .imageType("png")
                .category(Category.builder().name("cat").description("desc").build())
                .build();

        customer = Customer.builder()
                .address("tetouan")
                .firstName("bilal")
                .lastName("belhaj")
                .email("bilal@gmail.com")
                .appUser(AppUser.builder().username("bilal@gmail.com").password("password").build())
                .build();
    }

    // test place order
    @Test
    void canPlaceOrder() {
        // given
        customer.setId(1L);
        product.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        CreateItemDTO item = new CreateItemDTO(product.getId(), 2);
        List<CreateItemDTO> items = List.of(item);
        CreateOrderDTO dto = new CreateOrderDTO(customer.getId(), items ,customer.getAddress());

        // when
        OrderResponseDTO res = underTest.placeOrder(dto);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order saved = orderArgumentCaptor.getValue();

        // then
        assertThat(res).isNotNull();
        assertThat(res.orderSummary().id()).isEqualTo(saved.getId());
        assertThat(res.orderSummary().totalAmount()).isEqualTo(BigDecimal.valueOf(item.quantity()).multiply(product.getPrice()));
        verify(notificationService).send(any(CreateNotificationDTO.class));

    }

    @Test
    void placeOrder_Throws_whenCustomerNotExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        CreateOrderDTO dto = new CreateOrderDTO(1L, null ,"fake address");

        assertThatThrownBy(() -> underTest.placeOrder(dto)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void placeOrder_throws_whenNoItems() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CreateOrderDTO dto = new CreateOrderDTO(1L, List.of() ,"fake address");

        assertThatThrownBy(()->underTest.placeOrder(dto)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void PlaceOrder_throws_ProductNotExists() {
        product.setId(1L);
        customer.setId(1L);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        List<CreateItemDTO> items = List.of(
                new CreateItemDTO(product.getId(), 1), new CreateItemDTO(2L, 12)
        );
        CreateOrderDTO dto = new CreateOrderDTO(customer.getId(), items ,customer.getAddress());

        assertThatThrownBy(() -> underTest.placeOrder(dto)).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void PlaceOrder_throws_invalidStock() {
        product.setId(1L);
        customer.setId(1L);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        List<CreateItemDTO> items = List.of(
                new CreateItemDTO(product.getId(), 100)
        );
        CreateOrderDTO dto = new CreateOrderDTO(customer.getId(), items ,customer.getAddress());

        assertThatThrownBy(() -> underTest.placeOrder(dto)).isInstanceOf(BadRequestException.class);
    }


    @Test
    void getAllOrders() {
        order = Order.builder().date(LocalDateTime.now()).items(new ArrayList<>()).customer(customer).status(OrderStatus.CONFIRMED).build();
        order.getItems().add(OrderItem.builder().product(product).quantity(10).build());
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<OrderSummaryDTO> res = underTest.getAllOrders(null, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void getAllOrders_ByStatus() {
        order = Order.builder().date(LocalDateTime.now()).items(new ArrayList<>()).customer(customer).status(OrderStatus.CONFIRMED).build();
        order.getItems().add(OrderItem.builder().product(product).quantity(10).build());
        when(orderRepository.findByStatus(eq(OrderStatus.CONFIRMED), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order)));

        Page<OrderSummaryDTO> res = underTest.getAllOrders(OrderStatus.CONFIRMED, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void getCustomerOrders() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(orderRepository.findByCustomerId(eq(customer.getId()), any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(order))
        );

        Page<OrderSummaryDTO> res = underTest.getCustomerOrders(customer.getId(), null, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void getCustomerOrders_byStatus() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(orderRepository.findByCustomerIdAndStatus(eq(customer.getId()), eq(order.getStatus()) ,any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(order))
        );

        Page<OrderSummaryDTO> res = underTest.getCustomerOrders(customer.getId(), order.getStatus(), 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void getCustomerOrders_throws_whenCustomerNotExists() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> underTest.getCustomerOrders(1L, null, 0, 10)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getOrder() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .id(1L)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderResponseDTO res = underTest.getOrder(order.getId());

        assertThat(res).isNotNull();
        assertThat(res.orderSummary().id()).isEqualTo(order.getId());
    }

    @Test
    void getOrder_throws_whenNotExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getOrder(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void cancelOrder() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .id(1L)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );

        OrderSummaryDTO res = underTest.cancelOrder(order.getId());

        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(order.getId());
        assertThat(res.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void cancelOrder_throws_whenNotExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.cancelOrder(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void cancelOrder_throws_whenOrderDelivered() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .id(1L)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.DELIVERED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(()->underTest.cancelOrder(order.getId())).isInstanceOf(BadRequestException.class);
    }

    @Test
    void updateStatus() {
        customer.setId(1L);
        order = Order.builder()
                .customer(customer)
                .id(1L)
                .date(LocalDateTime.now())
                .shippingAddress(customer.getAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();
        order.getItems().add(
                OrderItem.builder().quantity(2).product(product).build()
        );
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );

        OrderSummaryDTO res = underTest.updateStatus(order.getId(), OrderStatus.DELIVERED);

        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(order.getId());
        assertThat(res.status()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void updateStatus_throws_whenOrderNotExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateStatus(1L, OrderStatus.CONFIRMED)).isInstanceOf(ResourceNotFoundException.class);
    }
}