package org.example.ecommerceapi.controller;

import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "CUSTOMER")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void getNotifications() throws Exception{
        List<NotificationResponse> res = List.of(
                new NotificationResponse("TEST", "test notification", 1L, false, LocalDateTime.now()),
                new NotificationResponse("TEST", "test notification 2", 2L, true, LocalDateTime.now().plusDays(1))
        ) ;

        when(notificationService.getNotifications(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/notifications/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(res.size()))
                .andExpect(jsonPath("$[0].type").value("TEST"))
                .andExpect(jsonPath("$[0].content").value(res.get(0).content()))
                .andExpect(jsonPath("$[0].isRead").value(false))
                .andExpect(jsonPath("$[0].createdAt").value(res.get(0).createdAt()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].isRead").value(true))
                .andExpect(jsonPath("$[1].createdAt").value(res.get(1).createdAt()));
    }

    @Test
    void read() {
    }

    @Test
    void delete() {
    }
}