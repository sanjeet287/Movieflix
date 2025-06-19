package com.movieflix.notificationservice.listener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.movieflix.notificationservice.config.RabbitMQConfig;
import com.movieflix.notificationservice.dto.NotificationRequest;
import com.movieflix.notificationservice.dto.UserEvent;
import com.movieflix.notificationservice.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(UserEvent event) {
    	log.info("Received event: {}", event);
        String message;
        String subject;

        switch (event.getEventType()) {
            case "SIGNUP" -> {
                subject = "Welcome to Movieflix!";
                message = "Hi " + event.getName() + ", thanks for signing up!";
            }
            case "LOGIN" -> {
                subject = "Login Notification";
                message = "Hello " + event.getName() + ", you just logged in.";
            }
            case "UPDATE_PROFILE" -> {
                subject = "Profile Updated";
                message = "Your profile has been successfully updated.";
            }
            case "DELETE" -> {
                subject = "Account Deleted";
                message = "We're sorry to see you go, " + event.getName() + ".";
            }
            default -> throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
        }

        NotificationRequest request = new NotificationRequest();
        request.setToEmail(event.getEmail());
        request.setSubject(subject);
        request.setTemplateName("emailTemplate");
        request.setVariables(Map.of(
            "username", event.getName(),
            "message", message,
            "actionUrl", "https://movieflix-clone.com/login"
        ));

        notificationService.sendNotification(request);
    }
}
