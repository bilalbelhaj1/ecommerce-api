package org.example.ecommerceapi.dto.notification;

/**
 * @author $(bilal belhaj)
 **/
public record CreateNotificationDTO(
        String type,
        String content,
        Long userId
) {
}
