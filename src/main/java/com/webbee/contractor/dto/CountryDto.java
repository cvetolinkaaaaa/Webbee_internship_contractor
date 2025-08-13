package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

/**
 * DTO для передачи данных о стране в REST API.
 * @author Evseeva Tsvetolina
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
    @Schema(description = "Уникальный id страны", example = "RUS")
    private String id;
    /**
     * Наименование страны.
     */
    @Schema(description = "Название страны", example = "Россия")
    private String name;
    /**
     * Признак активности страны.
     */
    @Schema(description = "Признак активности страны", example = "true", defaultValue = "true")
    @JsonProperty("is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

}
