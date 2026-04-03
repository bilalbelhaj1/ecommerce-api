package org.example.ecommerceapi.controller;

import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderItemResponse;
import org.example.ecommerceapi.dto.orderManagement.OrderResponseDTO;
import org.example.ecommerceapi.dto.orderManagement.OrderSummaryDTO;
import org.example.ecommerceapi.enums.OrderStatus;
import org.example.ecommerceapi.service.OrderManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class AdminOrderManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderManagementService orderManagementService;

    @Test
    void getOrders() throws Exception{
        Page<OrderSummaryDTO> res = new PageImpl<>(List.of(
                new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.TEN, OrderStatus.PROCESSING),
                new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.valueOf(20), OrderStatus.DELIVERED)
        ));

        when(orderManagementService.getAllOrders(null, 0, 10)).thenReturn(res);

        mockMvc.perform(get("/api/v1/admin/orders", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].totalAmount").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.content[1].id").value(1L))
                .andExpect(jsonPath("$.content[1].totalAmount").value(BigDecimal.valueOf(20)));
    }

    @Test
    void getOrder() throws Exception{
        OrderResponseDTO res = new OrderResponseDTO(
                new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.TEN, OrderStatus.PROCESSING),
                List.of(
                        new OrderItemResponse(BigDecimal.valueOf(1), 4, "prod1", 1L),
                        new OrderItemResponse(BigDecimal.valueOf(10), 4, "prod2", 2L)),
                new CustomerSummaryDTO(1L, "bilal", "belhaj")
        );

        when(orderManagementService.getOrder(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/admin/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderSummary.id").value(1L))
                .andExpect(jsonPath("$.orderSummary.totalAmount").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.customer.id").value(1L))
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    void getCustomerOrders() throws Exception{
        Page<OrderSummaryDTO> res = new PageImpl<>(List.of(
                new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.TEN, OrderStatus.PROCESSING),
                new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.valueOf(20), OrderStatus.DELIVERED)
        ));

        when(orderManagementService.getCustomerOrders(1L, null, 0, 10)).thenReturn(res);

        mockMvc.perform(get("/api/v1/admin/orders/customer/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].totalAmount").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.content[1].id").value(1L))
                .andExpect(jsonPath("$.content[1].totalAmount").value(BigDecimal.valueOf(20)));
    }

    @Test
    void updateStatus() throws Exception{
        OrderSummaryDTO res = new OrderSummaryDTO(1L, LocalDateTime.now(), "tetouan", BigDecimal.TEN, OrderStatus.DELIVERED);

        when(orderManagementService.updateStatus(1L, OrderStatus.DELIVERED)).thenReturn(res);

        mockMvc.perform(put("/api/v1/admin/orders/{id}", 1L).param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalAmount").value(BigDecimal.TEN));
    }
}