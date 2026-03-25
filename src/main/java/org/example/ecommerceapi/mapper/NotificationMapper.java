package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.entity.Notification;
import org.example.ecommerceapi.entity.User;

/**
 * @author $(bilal belhaj)
 **/
public class NotificationMapper {
    public static NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getType(),
                notification.getContent(),
                notification.getId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static Notification toEntity(CreateNotificationDTO dto, User user) {
        return Notification.builder()
                .type(dto.type())
                .content(dto.content())
                .user(user)
                .build();
    }
}
