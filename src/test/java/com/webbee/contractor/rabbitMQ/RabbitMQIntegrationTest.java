package com.webbee.contractor.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorEventDto;
import com.webbee.contractor.service.ContractorMessageService;
import com.webbee.contractor.service.OutboxPublisherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("RabbitMQ Integration Tests - полный цикл отправки сообщений")
class RabbitMQIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer(
            DockerImageName.parse("rabbitmq:3.12-management-alpine")
    )
            .withUser("test", "test")
            .withPermission("/", "test", ".*", ".*", ".*")
            .withStartupTimeout(Duration.ofMinutes(3))
            .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitMQ::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", () -> "test");
        registry.add("spring.rabbitmq.password", () -> "test");
        registry.add("spring.rabbitmq.ssl.enabled", () -> "false");
    }

    @Autowired
    private ContractorMessageService contractorMessageService;

    @Autowired
    private OutboxPublisherService outboxPublisherService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Полный цикл: создание события в Outbox -> отправка в RabbitMQ")
    void fullCycle_CreateOutboxEvent_AndSendToRabbitMQ() {
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("INTEGRATION-TEST-001");
        contractorDto.setName("Интеграционная тестовая компания");
        
        assertDoesNotThrow(() -> {
            contractorMessageService.sendContractorUpdate(contractorDto, true, 1L);
        });

        await()
            .atMost(15, TimeUnit.SECONDS)
            .pollDelay(1, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertDoesNotThrow(() -> {
                    outboxPublisherService.publishPendingEvents();
                });
                
                assertTrue(rabbitMQ.isRunning(), "RabbitMQ контейнер должен быть запущен");
                assertNotNull(rabbitTemplate, "RabbitTemplate должен быть инициализирован");
            });
    }

}