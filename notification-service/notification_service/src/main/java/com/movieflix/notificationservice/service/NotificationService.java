package com.movieflix.notificationservice.service;

import com.movieflix.notificationservice.dto.NotificationRequest;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
}
