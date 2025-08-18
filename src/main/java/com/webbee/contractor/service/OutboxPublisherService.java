package com.webbee.contractor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.config.RabbitConfig;
import com.webbee.contractor.dto.ContractorEventDto;
import com.webbee.contractor.model.OutboxEvent;
import com.webbee.contractor.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для отправки событий из Outbox в RabbitMQ.
 * Retry-логика реализована на уровне RabbitMQ через Dead Letter Exchanges.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final int BATCH_SIZE = 10;

    /**
     * Периодически отправляет необработанные события.
     * При ошибке RabbitTemplate автоматически применит retry-политику.
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findPendingEvents(10);

        if (!pendingEvents.isEmpty()) {
            log.info("Processing {} pending outbox events", pendingEvents.size());
        }

        for (OutboxEvent event : pendingEvents) {
            try {
                log.info("Publishing outbox event {} for contractor {}",
                        event.getId(), event.getAggregateId());

                publishEvent(event);

                outboxEventRepository.updateEventStatus(
                        event.getId(),
                        OutboxEvent.STATUS_PROCESSED,
                        LocalDateTime.now()
                );

                log.info("Successfully published and marked as processed outbox event {}", event.getId());

            } catch (Exception e) {
                log.error("Failed to publish outbox event {}. Will retry later. Error: {}",
                        event.getId(), e.getMessage(), e);
            }
        }
    }

    private void publishEvent(OutboxEvent event) throws Exception {
        log.info("Sending to RabbitMQ - Event ID: {}, Event Type: {}, JSON payload: {}",
                event.getId(), event.getEventType(), event.getEventData());

        ContractorEventDto eventDto = objectMapper.readValue(event.getEventData(), ContractorEventDto.class);

        String routingKey = event.getEventType().equals("CREATED") ?
                RabbitConfig.CONTRACTOR_CREATED_ROUTING_KEY :
                RabbitConfig.CONTRACTOR_UPDATED_ROUTING_KEY;

        log.info("Publishing to RabbitMQ - Exchange: {}, Routing Key: {}, Contractor ID: {}",
                RabbitConfig.CONTRACTORS_EXCHANGE, routingKey, eventDto.getContractorId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.CONTRACTORS_EXCHANGE,
                routingKey,
                eventDto
        );

        log.info("Message successfully sent to RabbitMQ for contractor {} with event type {}",
                eventDto.getContractorId(), event.getEventType());
    }

}
