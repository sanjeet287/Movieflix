package com.movieflix.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.movieflix.notificationservice.dto.NotificationRequest;
import com.movieflix.notificationservice.exception.NotificationSendException;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EmailNotificationService implements NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public void sendNotification(NotificationRequest request) {
		try {
			logger.info("Preparing HTML email to: {}", request.getToEmail());

			Context context = new Context();
			context.setVariables(request.getVariables());

			String htmlContent = templateEngine.process(request.getTemplateName(), context);

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setTo(request.getToEmail());
			helper.setSubject(request.getSubject());
			helper.setText(htmlContent, true);

			mailSender.send(mimeMessage);
			logger.info("HTML email sent successfully to {}", request.getToEmail());
		} catch (Exception e) {
			logger.error("Failed to send HTML email to {}: {}", request.getToEmail(), e.getMessage(), e);
			throw new NotificationSendException("Failed to send HTML email", e);
		}
	}
}