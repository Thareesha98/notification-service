package thareesha.campustalk.notification_service.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "campustalk.notifications.exchange";
    public static final String ROUTING_KEY = "campustalk.notifications.key";
    public static final String QUEUE = "campustalk.notifications.queue";

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(notificationQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY);
    }
}

