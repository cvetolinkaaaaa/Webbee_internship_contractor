package com.webbee.contractor.service;

import com.webbee.contractor.config.RabbitConfig;
import com.webbee.contractor.dto.ContractorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки сообщений о контрагентах в RabbitMQ.
 * @author Evseeva Tsvetolina
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContractorMessageService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Отправляет сообщение о создании нового контрагента в RabbitMQ.
     *
     * @param contractorDto DTO контрагента для отправки
     */
    public void sendContractorCreated(ContractorDto contractorDto) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitConfig.CONTRACTORS_EXCHANGE, 
                RabbitConfig.CONTRACTOR_CREATED_ROUTING_KEY, 
                contractorDto
            );
            log.info("Сообщение о создании контрагента с ID {} отправлено в RabbitMQ с routing key '{}'", 
                contractorDto.getId(), RabbitConfig.CONTRACTOR_CREATED_ROUTING_KEY);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения о создании контрагента с ID {} в RabbitMQ", 
                contractorDto.getId(), e);
        }
    }

    /**
     * Отправляет сообщение об обновлении контрагента в RabbitMQ.
     *
     * @param contractorDto DTO контрагента для отправки
     */
    public void sendContractorUpdated(ContractorDto contractorDto) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitConfig.CONTRACTORS_EXCHANGE, 
                RabbitConfig.CONTRACTOR_UPDATED_ROUTING_KEY, 
                contractorDto
            );
            log.info("Сообщение об обновлении контрагента с ID {} отправлено в RabbitMQ с routing key '{}'", 
                contractorDto.getId(), RabbitConfig.CONTRACTOR_UPDATED_ROUTING_KEY);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения об обновлении контрагента с ID {} в RabbitMQ", 
                contractorDto.getId(), e);
        }
    }

    /**
     * Отправляет общее сообщение об изменении контрагента с автоматическим определением routing key.
     *
     * @param contractorDto DTO контрагента для отправки
     * @param isNew true если контрагент новый, false если обновляется существующий
     */
    public void sendContractorUpdate(ContractorDto contractorDto, boolean isNew) {
        if (isNew) {
            sendContractorCreated(contractorDto);
        } else {
            sendContractorUpdated(contractorDto);
        }
    }

    /**
     * Отправляет сообщение об изменении контрагента (устаревший метод для совместимости).
     * По умолчанию считает, что это обновление существующего контрагента.
     *
     * @param contractorDto DTO контрагента для отправки
     */
    public void sendContractorUpdate(ContractorDto contractorDto) {
        sendContractorUpdated(contractorDto);
    }
}