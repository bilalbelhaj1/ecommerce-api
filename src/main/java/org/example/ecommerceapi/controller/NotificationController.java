package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.notification.NotificationResponse;
import org.example.ecommerceapi.service.NotificationService;
import org.example.ecommerceapi.service.impl.NotificationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/
@Tag(name = "notifications", description = "operations related to notifications")
@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
@RestController
@RequestMapping ("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // get
    @GetMapping("{id}")
    @Operation(
            summary = "get notifications",
            description = "returns all user notifications"
    )
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @Parameter(description = "User Id") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(notificationService.getNotifications(id));
    }

    // read
    @PutMapping("{id}")
    @Operation(
            summary = "read notification",
            description = "mark notification as read"
    )
    public ResponseEntity<NotificationResponse> read(@Parameter(description = "Notification Id") @PathVariable Long id) {
        return ResponseEntity.ok().body(notificationService.read(id));
    }

    // delete
    @DeleteMapping("{id}")
    @Operation(
            summary = "delete notification",
            description = "Delete notification"
    )
    public ResponseEntity<Void> delete(@Parameter(description = "Notification Id") @PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
