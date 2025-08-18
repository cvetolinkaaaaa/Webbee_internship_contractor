package com.webbee.contractor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация RabbitMQ для отправки сообщений о контрагентах с поддержкой retry-механизма.
 * @author Evseeva Tsvetolina
 */
@Configuration
public class RabbitConfig {

    public static final String CONTRACTORS_EXCHANGE = "contractors_contractor_exchange";
    public static final String CONTRACTOR_CREATED_ROUTING_KEY = "contractor.created";
    public static final String CONTRACTOR_UPDATED_ROUTING_KEY = "contractor.updated";

    public static final String DEALS_CONTRACTOR_QUEUE = "deals_contractor_queue";

    public static final String DEALS_DEAD_EXCHANGE = "deals_dead_exchange";
    public static final String DEALS_CONTRACTOR_DEAD_QUEUE = "deals_contractor_dead_queue";

    public static final String DEAL_CONTRACTOR_DEAD_EXCHANGE = "deal_contractor_dead_exchange";

    public static final int RETRY_TTL = 5 * 60 * 1000;

    @Bean
    public TopicExchange contractorsExchange() {
        return new TopicExchange(CONTRACTORS_EXCHANGE);
    }

    @Bean
    public TopicExchange dealsDeadExchange() {
        return new TopicExchange(DEALS_DEAD_EXCHANGE);
    }

    @Bean
    public TopicExchange dealContractorDeadExchange() {
        return new TopicExchange(DEAL_CONTRACTOR_DEAD_EXCHANGE);
    }

    /**
     * Основная очередь для обработки сообщений о контрагентах.
     * При ошибке сообщения отправляются в deals_dead_exchange.
     */
    @Bean
    public Queue dealsContractorQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEALS_DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key", "contractor.dead");
        return QueueBuilder.durable(DEALS_CONTRACTOR_QUEUE)
                .withArguments(args)
                .build();
    }

    /**
     * Очередь для "мертвых" сообщений с TTL для retry-механизма.
     * После истечения TTL сообщения отправляются в deal_contractor_dead_exchange для повторной попытки.
     */
    @Bean
    public Queue dealsContractorDeadQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", RETRY_TTL);
        args.put("x-dead-letter-exchange", DEAL_CONTRACTOR_DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key", "contractor.retry");
        return QueueBuilder.durable(DEALS_CONTRACTOR_DEAD_QUEUE)
                .withArguments(args)
                .build();
    }

    /**
     * Привязка основной очереди к exchange контрагентов.
     * Получает все сообщения о контрагентах (создание и обновление).
     */
    @Bean
    public Binding dealsContractorBinding() {
        return BindingBuilder.bind(dealsContractorQueue())
                .to(contractorsExchange())
                .with("contractor.*");
    }

    /**
     * Привязка dead queue к deals_dead_exchange для получения отклоненных сообщений.
     * Получает "мертвые" сообщения для ожидания retry.
     */
    @Bean
    public Binding dealsContractorDeadBinding() {
        return BindingBuilder.bind(dealsContractorDeadQueue())
                .to(dealsDeadExchange())
                .with("contractor.dead");
    }

    /**
     * Привязка для retry-механизма.
     * После истечения TTL сообщения возвращаются в основную очередь для повторной попытки.
     * Исправлена привязка: теперь используется deal_contractor_dead_exchange -> deals_contractor_queue
     */
    @Bean
    public Binding dealContractorRetryBinding() {
        return BindingBuilder.bind(dealsContractorQueue())
                .to(dealContractorDeadExchange())
                .with("contractor.retry");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
