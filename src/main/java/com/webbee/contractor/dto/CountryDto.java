package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

/**
 * DTO для передачи данных о стране в REST API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

}
