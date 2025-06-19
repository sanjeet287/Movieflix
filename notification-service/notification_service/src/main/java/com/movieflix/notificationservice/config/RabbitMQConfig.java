package com.movieflix.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    public static final String USER_SIGNUP_ROUTING_KEY = "user.signup";
    public static final String USER_LOGIN_ROUTING_KEY = "user.login";
    public static final String USER_UPDATE_ROUTING_KEY = "user.update";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete";
    
    

        @Bean
        public MessageConverter messageConverter() {
            return new Jackson2JsonMessageConverter();
        }
        
        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(messageConverter);
            return factory;
        }

        @Bean
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(messageConverter());
            return template;
        }
    


    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding signupBinding() {
        return BindingBuilder.bind(notificationQueue())
            .to(userExchange())
            .with(USER_SIGNUP_ROUTING_KEY);
    }

    @Bean
    public Binding loginBinding() {
        return BindingBuilder.bind(notificationQueue())
            .to(userExchange())
            .with(USER_LOGIN_ROUTING_KEY);
    }

    @Bean
    public Binding updateBinding() {
        return BindingBuilder.bind(notificationQueue())
            .to(userExchange())
            .with(USER_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding deleteBinding() {
        return BindingBuilder.bind(notificationQueue())
            .to(userExchange())
            .with(USER_DELETE_ROUTING_KEY);
    }
}


