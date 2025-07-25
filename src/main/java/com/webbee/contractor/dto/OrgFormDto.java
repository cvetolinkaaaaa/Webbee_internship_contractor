package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных об организации в REST API.
 * @author Evseeva Tsvetolina
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrgFormDto {

    /**
     * Уникальный id организации.
     */
    @Schema(description = "Уникальный id организации", example = "1")
    private Integer id;
    /**
     * Наименование организации.
     */
    @Schema(description = "Название организации", example = "Политические партии")
    private String name;
    /**
     * Признак активности организации.
     */
    @Schema(description = "Признак активности организации", example = "true", defaultValue = "true")
    @JsonProperty("is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

}
