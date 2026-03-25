package org.example.ecommerceapi.dto.notification;

import java.time.LocalDateTime;

/**
 * @author $(bilal belhaj)
 **/
public record NotificationResponse (
        String type,
        String content,
        Long id,
        boolean isRead,
        LocalDateTime createdAt
) {
}
