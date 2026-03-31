package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.entity.AppUser;
import org.example.ecommerceapi.entity.Notification;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.NotificationRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.example.ecommerceapi.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock private NotificationRepository notificationRepo;
    @Mock private UserRepository userRepo;
    @InjectMocks private NotificationServiceImpl underTest;

    private CreateNotificationDTO dto;
    private AppUser user;
    private Notification notif;

    @BeforeEach
    void setUp() {
        dto = new CreateNotificationDTO("TEST", "foo", 1L);
        user  = AppUser.builder().username("bilal@gmail.com").username("password").build();
        notif = Notification.builder()
                .user(user)
                .type("TEST")
                .content("foo")
                .isRead(false)
                .build();
    }

    @Test
    void canSendNotification_whenUserExists(){
        // given
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepo.save(any(Notification.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );

        // when
        NotificationResponse res = underTest.send(dto);
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        // then
        verify(notificationRepo).save(notificationArgumentCaptor.capture());
        Notification saved = notificationArgumentCaptor.getValue();
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(saved.getId());
    }

    @Test
    void sendNotification_throws_whenUserNotFound(){
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.send(dto)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canGetNotifications_forValidUserId(){
        // given
        user.setId(1L);
        when(userRepo.existsById(1L)).thenReturn(true);
        when(notificationRepo.findByUserId(1L)).thenReturn(List.of(notif));
        // when
        List<NotificationResponse> res = underTest.getNotifications(1L);
        // then
        assertThat(res).isNotEmpty();
        assertThat(res.size()).isEqualTo(1);

    }

    @Test
    void getNotifications_throws_whenUserIdIsNull(){
        assertThatThrownBy(() -> underTest.getNotifications(null)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void getNotifications_throws_whenUserNotExists(){
        assertThatThrownBy(() -> underTest.getNotifications(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canReadNotification_whenNotificationExists(){
        notif.setId(1L);
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notif));
        when(notificationRepo.save(any(Notification.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        NotificationResponse res = underTest.read(1L);

        assertThat(res).isNotNull();
        assertThat(res.isRead()).isTrue();
    }

    @Test
    void readNotification_throws_whenNotificationNotFound(){
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.read(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canDeleteNotification_whenNotificationExists(){
        notif.setId(1L);
        when(notificationRepo.existsById(1L)).thenReturn(true);

        underTest.delete(1L);

        verify(notificationRepo).deleteById(1L);
    }

    @Test
    void deleteNotification_throws_whenNotificationNotFound(){
        assertThatThrownBy(() -> underTest.delete(1L)).isInstanceOf(ResourceNotFoundException.class);
    }
}