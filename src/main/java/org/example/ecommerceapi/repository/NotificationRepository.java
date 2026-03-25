package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long id);
}
