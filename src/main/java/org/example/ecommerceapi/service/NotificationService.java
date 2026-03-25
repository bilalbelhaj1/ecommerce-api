package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.notification.CreateNotificationDTO;
import org.example.ecommerceapi.dto.notification.NotificationResponse;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface NotificationService {
    NotificationResponse send(CreateNotificationDTO dto);
    List<NotificationResponse> getNotifications(Long userId);
    NotificationResponse read(Long id);
    void delete(Long id);
}
