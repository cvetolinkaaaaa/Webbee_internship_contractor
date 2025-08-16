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
     * Отправляет сообщение об изменении контрагента в RabbitMQ.
     *
     * @param contractorDto DTO контрагента для отправки
     */
    public void sendContractorUpdate(ContractorDto contractorDto) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.CONTRACTORS_EXCHANGE,
                    RabbitConfig.CONTRACTOR_ROUTING_KEY,contractorDto);
            log.info("Сообщение о контрагенте с ID {} отправлено в RabbitMQ", contractorDto.getId());
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения о контрагенте с ID {} в RabbitMQ", contractorDto.getId(), e);
        }
    }
}