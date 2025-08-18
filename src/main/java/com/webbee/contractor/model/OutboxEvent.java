package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Модель для хранения исходящих событий (Outbox Pattern).
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("outbox_event")
public class OutboxEvent {

    @Id
    private String id;

    /** Идентификатор агрегата (contractor_id) */
    private String aggregateId;

    /** Тип агрегата */
    private String aggregateType;

    /** Тип события */
    private String eventType;

    /** Версия агрегата на момент события */
    private Long version;

    /** Данные события в JSON */
    private String eventData;

    /** Дата создания события */
    private LocalDateTime createdAt;

    /** Дата успешной отправки */
    private LocalDateTime processedAt;

    /** Статус обработки - используем String вместо enum для совместимости с Spring Data JDBC */
    private String status;

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSED = "PROCESSED";

    public boolean isPending() {
        return STATUS_PENDING.equals(status);
    }

    public boolean isProcessed() {
        return STATUS_PROCESSED.equals(status);
    }

    public void setStatusPending() {
        this.status = STATUS_PENDING;
    }

    public void setStatusProcessed() {
        this.status = STATUS_PROCESSED;
    }

    public static class OutboxEventBuilder {
        public OutboxEvent build() {
            if (this.id == null) {
                this.id = UUID.randomUUID().toString();
            }
            if (this.status == null) {
                this.status = STATUS_PENDING;
            }
            return new OutboxEvent(id, aggregateId, aggregateType, eventType,
                    version, eventData, createdAt, processedAt, status);
        }
    }

}
