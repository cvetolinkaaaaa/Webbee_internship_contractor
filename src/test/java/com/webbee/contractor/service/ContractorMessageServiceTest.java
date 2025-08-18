
package com.webbee.contractor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorEventDto;
import com.webbee.contractor.model.OutboxEvent;
import com.webbee.contractor.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ContractorMessageService - тесты для создания событий в Outbox")
class ContractorMessageServiceTest {

    private ContractorMessageService contractorMessageService;
    private OutboxEventRepository outboxEventRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        outboxEventRepository = mock(OutboxEventRepository.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        contractorMessageService = new ContractorMessageService(outboxEventRepository, objectMapper);
    }

    @Test
    @DisplayName("sendContractorUpdate() создает событие CREATED для нового контрагента")
    void sendContractorUpdate_CreateEvent_ForNewContractor() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-001");
        contractorDto.setName("Тестовая компания");
        Long version = 1L;
        boolean isNewContractor = true;

        contractorMessageService.sendContractorUpdate(contractorDto, isNewContractor, version);

        ArgumentCaptor<String> eventDataCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> eventTypeCaptor = ArgumentCaptor.forClass(String.class);
        
        verify(outboxEventRepository).insertEvent(
                anyString(),
                eq("TEST-001"),
                eq("CONTRACTOR"),
                eventTypeCaptor.capture(),
                eq(version),
                eventDataCaptor.capture(),
                any(LocalDateTime.class),
                eq(OutboxEvent.STATUS_PENDING)
        );

        assertEquals("CREATED", eventTypeCaptor.getValue());

        String eventDataJson = eventDataCaptor.getValue();
        ContractorEventDto eventDto = objectMapper.readValue(eventDataJson, ContractorEventDto.class);
        
        assertEquals("TEST-001", eventDto.getContractorId());
        assertEquals(version, eventDto.getVersion());
        assertEquals("CREATED", eventDto.getEventType());
        assertEquals("Тестовая компания", eventDto.getContractorData().getName());
        assertNotNull(eventDto.getEventTime());
    }

    @Test
    @DisplayName("sendContractorUpdate() создает событие UPDATED для существующего контрагента")
    void sendContractorUpdate_CreateEvent_ForExistingContractor() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-002");
        contractorDto.setName("Обновленная компания");
        Long version = 5L;
        boolean isNewContractor = false;

        contractorMessageService.sendContractorUpdate(contractorDto, isNewContractor, version);

        ArgumentCaptor<String> eventTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> versionCaptor = ArgumentCaptor.forClass(Long.class);
        
        verify(outboxEventRepository).insertEvent(
                anyString(),
                eq("TEST-002"),
                eq("CONTRACTOR"),
                eventTypeCaptor.capture(),
                versionCaptor.capture(),
                anyString(),
                any(LocalDateTime.class),
                eq(OutboxEvent.STATUS_PENDING)
        );

        assertEquals("UPDATED", eventTypeCaptor.getValue());
        assertEquals(5L, versionCaptor.getValue());
    }

    @Test
    @DisplayName("sendContractorUpdate() выбрасывает исключение при ошибке сериализации")
    void sendContractorUpdate_ThrowsException_WhenSerializationFails() throws Exception {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("TEST-003");
        ObjectMapper faultyMapper = mock(ObjectMapper.class);
        when(faultyMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Serialization error"));
        
        ContractorMessageService service = new ContractorMessageService(outboxEventRepository, faultyMapper);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> service.sendContractorUpdate(contractorDto, true, 1L));
        
        assertEquals("Failed to create outbox event", exception.getMessage());
        verify(outboxEventRepository, never()).insertEvent(anyString(), anyString(), anyString(), 
            anyString(), anyLong(), anyString(), any(LocalDateTime.class), anyString());
    }
}