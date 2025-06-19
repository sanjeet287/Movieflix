package com.movieflix.subscriptionservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String SUBSCRIPTION_EXCHANGE = "subscription.exchange";
	public static final String SUBSCRIPTION_ROUTING_KEY = "subscription.event";
	public static final String SUBSCRIPTION_QUEUE = "subscription.queue";

	@Bean
	public Queue subscriptionQueue() {
		return new Queue(SUBSCRIPTION_QUEUE, true);
	}

	@Bean
	public DirectExchange subscriptionExchange() {
		return new DirectExchange(SUBSCRIPTION_EXCHANGE);
	}

	@Bean
	public Binding subscriptionBinding() {
		return BindingBuilder.bind(subscriptionQueue()).to(subscriptionExchange()).with(SUBSCRIPTION_ROUTING_KEY);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

}
