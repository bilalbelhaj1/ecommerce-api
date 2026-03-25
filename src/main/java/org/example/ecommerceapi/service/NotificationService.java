package org.example.ecommerceapi.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.entity.Notification;
import org.example.ecommerceapi.entity.User;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.NotificationRepository;
import org.example.ecommerceapi.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author $(bilal belhaj)
 **/
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // send notification
    public NotificationResponse send(CreateNotificationDTO dto){
        User user = userRepository.findById(dto.userId()).orElseThrow(
                () -> new ResourceNotFoundException("User Not found")
        );

        Notification notification = toEntity(dto, user);

        return toResponse(notificationRepository.save(notification));
    }

    // get notifications

    // read notification

    // delete notification

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getType(),
                notification.getContent(),
                notification.getId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    private Notification toEntity(CreateNotificationDTO dto, User user) {
        return Notification.builder()
                .type(dto.type())
                .content(dto.content())
                .user(user)
                .build();
    }
}
