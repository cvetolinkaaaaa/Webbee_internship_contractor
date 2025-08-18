package com.webbee.contractor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorEventDto;
import com.webbee.contractor.model.OutboxEvent;
import com.webbee.contractor.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сервис для создания событий в Outbox.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContractorMessageService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Создает событие в Outbox вместо прямой отправки сообщения.
     */
    @Transactional
    public void sendContractorUpdate(ContractorDto contractorDto, boolean isNewContractor, Long version) {
        try {
            String eventType = isNewContractor ? "CREATED" : "UPDATED";

            ContractorEventDto eventDto = ContractorEventDto.builder()
                    .contractorId(contractorDto.getId())
                    .version(version)
                    .eventType(eventType)
                    .contractorData(contractorDto)
                    .eventTime(LocalDateTime.now())
                    .build();

            String eventData = objectMapper.writeValueAsString(eventDto);

            log.info("Creating outbox event for contractor {}. Event data JSON: {}",
                    contractorDto.getId(), eventData);

            outboxEventRepository.insertEvent(
                    UUID.randomUUID().toString(),
                    contractorDto.getId(),
                    "CONTRACTOR",
                    eventType,
                    version,
                    eventData,
                    LocalDateTime.now(),
                    OutboxEvent.STATUS_PENDING
            );

            log.info("Outbox event created successfully for contractor {} with version {}",
                    contractorDto.getId(), version);

        } catch (Exception e) {
            log.error("Failed to create outbox event for contractor {}", contractorDto.getId(), e);
            throw new RuntimeException("Failed to create outbox event", e);
        }
    }

}
