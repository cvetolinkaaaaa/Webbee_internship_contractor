package com.webbee.contractor.repository;

import com.webbee.contractor.model.OutboxEvent;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxEventRepository extends CrudRepository<OutboxEvent, String> {

    /**
     * Найти необработанные события для отправки.
     */
    @Query("SELECT * FROM outbox_event WHERE status = 'PENDING' ORDER BY created_at ASC LIMIT :limit")
    List<OutboxEvent> findPendingEvents(@Param("limit") int limit);

    /**
     * Вставить новое событие в outbox.
     */
    @Modifying
    @Query("INSERT INTO outbox_event (id, aggregate_id, aggregate_type, event_type, version, event_data, created_at, status) " +
            "VALUES (:id, :aggregateId, :aggregateType, :eventType, :version, :eventData, :createdAt, :status)")
    void insertEvent(@Param("id") String id,
                     @Param("aggregateId") String aggregateId,
                     @Param("aggregateType") String aggregateType,
                     @Param("eventType") String eventType,
                     @Param("version") Long version,
                     @Param("eventData") String eventData,
                     @Param("createdAt") LocalDateTime createdAt,
                     @Param("status") String status);

    /**
     * Обновить статус события после успешной отправки.
     */
    @Modifying
    @Query("UPDATE outbox_event SET status = :status, processed_at = :processedAt WHERE id = :id")
    void updateEventStatus(@Param("id") String id,
                           @Param("status") String status,
                           @Param("processedAt") LocalDateTime processedAt);

}
