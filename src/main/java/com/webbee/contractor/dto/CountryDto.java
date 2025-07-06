package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных о стране в REST API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDto {

    /**
     * Уникальный идентификатор страны.
     */
    private String id;
    /**
     * Наименование страны.
     */
    private String name;
    /**
     * Признак активности страны.
     */
    @JsonProperty("is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
