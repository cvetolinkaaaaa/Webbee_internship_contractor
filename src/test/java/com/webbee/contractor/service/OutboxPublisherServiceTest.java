
package com.webbee.contractor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webbee.contractor.config.RabbitConfig;
import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorEventDto;
import com.webbee.contractor.model.OutboxEvent;
import com.webbee.contractor.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OutboxPublisherService - тесты для отправки событий в RabbitMQ")
class OutboxPublisherServiceTest {

    private OutboxPublisherService outboxPublisherService;
    private OutboxEventRepository outboxEventRepository;
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        outboxEventRepository = mock(OutboxEventRepository.class);
        rabbitTemplate = mock(RabbitTemplate.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        outboxPublisherService = new OutboxPublisherService(
            outboxEventRepository, 
            rabbitTemplate, 
            objectMapper
        );
    }

    @Test
    @DisplayName("publishPendingEvents() обрабатывает события CREATED")
    void publishPendingEvents_ProcessesCreatedEvents() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-001");
        contractorDto.setName("Тестовая компания");

        ContractorEventDto eventDto = ContractorEventDto.builder()
                .contractorId("TEST-001")
                .version(1L)
                .eventType("CREATED")
                .contractorData(contractorDto)
                .eventTime(LocalDateTime.now())
                .build();

        String eventDataJson = objectMapper.writeValueAsString(eventDto);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId("event-001");
        outboxEvent.setAggregateId("TEST-001");
        outboxEvent.setEventType("CREATED");
        outboxEvent.setEventData(eventDataJson);
        outboxEvent.setStatus(OutboxEvent.STATUS_PENDING);

        when(outboxEventRepository.findPendingEvents(10))
                .thenReturn(Arrays.asList(outboxEvent));

        outboxPublisherService.publishPendingEvents();

        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ContractorEventDto> messageCaptor = ArgumentCaptor.forClass(ContractorEventDto.class);

        verify(rabbitTemplate).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals(RabbitConfig.CONTRACTORS_EXCHANGE, exchangeCaptor.getValue());
        assertEquals(RabbitConfig.CONTRACTOR_CREATED_ROUTING_KEY, routingKeyCaptor.getValue());
        
        ContractorEventDto sentMessage = messageCaptor.getValue();
        assertEquals("TEST-001", sentMessage.getContractorId());
        assertEquals("CREATED", sentMessage.getEventType());
        assertEquals(1L, sentMessage.getVersion());

        verify(outboxEventRepository).updateEventStatus(
                eq("event-001"),
                eq(OutboxEvent.STATUS_PROCESSED),
                any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("publishPendingEvents() обрабатывает события UPDATED")
    void publishPendingEvents_ProcessesUpdatedEvents() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-002");
        contractorDto.setName("Обновленная компания");

        ContractorEventDto eventDto = ContractorEventDto.builder()
                .contractorId("TEST-002")
                .version(3L)
                .eventType("UPDATED")
                .contractorData(contractorDto)
                .eventTime(LocalDateTime.now())
                .build();

        String eventDataJson = objectMapper.writeValueAsString(eventDto);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId("event-002");
        outboxEvent.setAggregateId("TEST-002");
        outboxEvent.setEventType("UPDATED");
        outboxEvent.setEventData(eventDataJson);

        when(outboxEventRepository.findPendingEvents(10))
                .thenReturn(Arrays.asList(outboxEvent));

        outboxPublisherService.publishPendingEvents();

        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitConfig.CONTRACTORS_EXCHANGE),
                routingKeyCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals(RabbitConfig.CONTRACTOR_UPDATED_ROUTING_KEY, routingKeyCaptor.getValue());
        
        Object sentMessage = messageCaptor.getValue();
        assertInstanceOf(ContractorEventDto.class, sentMessage);
        ContractorEventDto eventMessage = (ContractorEventDto) sentMessage;
        assertEquals("TEST-002", eventMessage.getContractorId());
        assertEquals("UPDATED", eventMessage.getEventType());
    }

    @Test
    @DisplayName("publishPendingEvents() не обновляет статус при ошибке отправки")
    void publishPendingEvents_DoesNotUpdateStatus_WhenSendingFails() throws Exception {
        ContractorEventDto eventDto = ContractorEventDto.builder()
                .contractorId("TEST-003")
                .version(1L)
                .eventType("CREATED")
                .contractorData(new ContractorDto())
                .eventTime(LocalDateTime.now())
                .build();

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId("event-003");
        outboxEvent.setEventType("CREATED");
        outboxEvent.setEventData(objectMapper.writeValueAsString(eventDto));

        when(outboxEventRepository.findPendingEvents(10))
                .thenReturn(Arrays.asList(outboxEvent));
        
        doThrow(new RuntimeException("RabbitMQ connection failed"))
                .when(rabbitTemplate).convertAndSend(
                    anyString(),
                    anyString(),
                    any(Object.class)
                );

        outboxPublisherService.publishPendingEvents();

        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));
        
        verify(outboxEventRepository, never()).updateEventStatus(
                anyString(), anyString(), any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("publishPendingEvents() ничего не делает при отсутствии событий")
    void publishPendingEvents_DoesNothing_WhenNoEvents() {
        when(outboxEventRepository.findPendingEvents(10))
                .thenReturn(Collections.emptyList());

        outboxPublisherService.publishPendingEvents();

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
        verify(outboxEventRepository, never()).updateEventStatus(
                anyString(), anyString(), any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("publishPendingEvents() обрабатывает пакет событий")
    void publishPendingEvents_ProcessesMultipleEvents() throws Exception {
        List<OutboxEvent> events = Arrays.asList(
            createTestEvent("event-001", "TEST-001", "CREATED"),
            createTestEvent("event-002", "TEST-002", "UPDATED")
        );

        when(outboxEventRepository.findPendingEvents(10)).thenReturn(events);

        outboxPublisherService.publishPendingEvents();

        verify(rabbitTemplate, times(2)).convertAndSend(anyString(), anyString(), any(Object.class));
        verify(outboxEventRepository, times(2)).updateEventStatus(
                anyString(), eq(OutboxEvent.STATUS_PROCESSED), any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("publishPendingEvents() корректно обрабатывает JSON десериализацию")
    void publishPendingEvents_HandlesJsonDeserializationCorrectly() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-004");
        contractorDto.setName("JSON Тест");

        ContractorEventDto originalEventDto = ContractorEventDto.builder()
                .contractorId("TEST-004")
                .version(2L)
                .eventType("CREATED")
                .contractorData(contractorDto)
                .eventTime(LocalDateTime.now())
                .build();

        String eventDataJson = objectMapper.writeValueAsString(originalEventDto);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId("event-004");
        outboxEvent.setAggregateId("TEST-004");
        outboxEvent.setEventType("CREATED");
        outboxEvent.setEventData(eventDataJson);
        outboxEvent.setStatus(OutboxEvent.STATUS_PENDING);

        when(outboxEventRepository.findPendingEvents(10))
                .thenReturn(Arrays.asList(outboxEvent));

        outboxPublisherService.publishPendingEvents();

        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(
                anyString(), 
                anyString(), 
                messageCaptor.capture()
        );

        Object capturedMessage = messageCaptor.getValue();
        assertInstanceOf(ContractorEventDto.class, capturedMessage);
        
        ContractorEventDto deserializedDto = (ContractorEventDto) capturedMessage;
        assertEquals("TEST-004", deserializedDto.getContractorId());
        assertEquals("CREATED", deserializedDto.getEventType());
        assertEquals(2L, deserializedDto.getVersion());
        assertEquals("JSON Тест", deserializedDto.getContractorData().getName());
        assertNotNull(deserializedDto.getEventTime());
    }

    private OutboxEvent createTestEvent(String eventId, String contractorId, String eventType) throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId(contractorId);
        contractorDto.setName("Test Company");

        ContractorEventDto eventDto = ContractorEventDto.builder()
                .contractorId(contractorId)
                .version(1L)
                .eventType(eventType)
                .contractorData(contractorDto)
                .eventTime(LocalDateTime.now())
                .build();

        OutboxEvent event = new OutboxEvent();
        event.setId(eventId);
        event.setAggregateId(contractorId);
        event.setEventType(eventType);
        event.setEventData(objectMapper.writeValueAsString(eventDto));
        event.setStatus(OutboxEvent.STATUS_PENDING);

        return event;
    }
}