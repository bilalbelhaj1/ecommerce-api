package org.example.ecommerceapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@RestController
@RequestMapping ("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // get
    @GetMapping("{id}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(notificationService.getNotifications(id));
    }

    // read
    @PutMapping("{id}")
    public ResponseEntity<NotificationResponse> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(notificationService.read(id));
    }

    // delete 
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
