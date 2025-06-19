package com.movieflix.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_SIGNUP_ROUTING_KEY = "user.signup";
    public static final String USER_LOGIN_ROUTING_KEY = "user.login";
    public static final String USER_UPDATE_ROUTING_KEY = "user.update";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete";
    
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



    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }
    @Bean
    public Queue signupQueue() { return new Queue("user.signup.queue"); }

    @Bean
    public Queue loginQueue() { return new Queue("user.login.queue"); }

    @Bean
    public Queue updateQueue() { return new Queue("user.update.queue"); }

    @Bean
    public Queue deleteQueue() { return new Queue("user.delete.queue"); }    

    @Bean
    public Binding signupBinding() {
        return BindingBuilder.bind(signupQueue()).to(userExchange()).with("user.signup");
    }

    @Bean
    public Binding loginBinding() {
        return BindingBuilder.bind(loginQueue()).to(userExchange()).with("user.login");
    }

    @Bean
    public Binding updateBinding() {
        return BindingBuilder.bind(updateQueue()).to(userExchange()).with("user.update");
    }

    @Bean
    public Binding deleteBinding() {
        return BindingBuilder.bind(deleteQueue()).to(userExchange()).with("user.delete");
    }

}
