package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    private AppUser user;
    @BeforeEach
    void setUp() {
        user = userRepository.save(
                AppUser.builder()
                        .username("bilal@gmail.com")
                        .password("password")
                        .build()
        );
    }

    @Test
    void findByUserId_ReturnsNotifications_whenExists() {
        // given
        Notification notification = Notification.builder()
                .content("notification is being tested")
                .type("TEST")
                .user(user)
                .build();
        Notification saved = notificationRepository.save(notification);
        // when
        List<Notification> res = notificationRepository.findByUserId(user.getId());
        // then
        assertThat(res).isNotEmpty();
    }

    @Test
    void findByUserId_ReturnsEmptyList_whenNotExists() {
        // when
        List<Notification> res = notificationRepository.findByUserId(user.getId());
        // then
        assertThat(res).isEmpty();
    }
}