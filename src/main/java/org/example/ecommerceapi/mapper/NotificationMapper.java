package org.example.ecommerceapi.mapper;

import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.entity.Notification;
import org.example.ecommerceapi.entity.AppUser;

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

    public static Notification toEntity(CreateNotificationDTO dto, AppUser appUser) {
        return Notification.builder()
                .type(dto.type())
                .content(dto.content())
                .user(appUser)
                .build();
    }
}
