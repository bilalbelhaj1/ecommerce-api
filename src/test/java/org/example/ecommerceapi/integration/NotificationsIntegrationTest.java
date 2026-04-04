package org.example.ecommerceapi.integration;

import jakarta.transaction.Transactional;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Customer;
import org.example.ecommerceapi.entity.Notification;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.NotificationRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = {"ADMIN", "CUSTOMER"})
public class NotificationsIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getNotifications_success() throws Exception {
        AppUser user = userRepository.save(AppUser.builder().username("bilal@gmail.com").password("password").build());

        List<Notification> notifs = List.of(
                Notification.builder().type("TEST").content("test").user(user).build(),
                Notification.builder().type("TEST").content("test2").user(user).build()
        );

        List<Notification> saved = notificationRepository.saveAll(notifs);

        mockMvc.perform(get("/api/v1/notifications/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(saved.size()))
                .andExpect(jsonPath("$[0].id").value(saved.get(0).getId()))
                .andExpect(jsonPath("$[0].content").value(saved.get(0).getContent()))
                .andExpect(jsonPath("$[1].id").value(saved.get(1).getId()))
                .andExpect(jsonPath("$[1].content").value(saved.get(1).getContent()));

    }

    @Test
    void readNotification_success() throws Exception {
        AppUser user = userRepository.save(AppUser.builder().username("bilal@gmail.com").password("password").build());

        Notification saved = notificationRepository.save(Notification.builder().type("TEST").content("test").isRead(false).user(user).build());

        mockMvc.perform(put("/api/v1/notifications/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.content").value(saved.getContent()))
                .andExpect(jsonPath("$.isRead").value(true));
    }

    @Test
    void deleteNotification_success() throws Exception {
        AppUser user = userRepository.save(AppUser.builder().username("bilal@gmail.com").password("password").build());
        Notification saved = notificationRepository.save(Notification.builder().type("TEST").content("test").isRead(false).user(user).build());

        mockMvc.perform(delete("/api/v1/notifications/{id}", saved.getId()))
                .andExpect(status().isNoContent());

    }

}
