package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных об индустрии в REST API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class IndustryDto {


    /**
     * Уникальный id индустрии.
     */
    @Schema(description = "Уникальный id индустрии", example = "1")
    private Integer id;
    /**
     * Наименование индустрии.
     */
    @Schema(description = "Название индустрии", example = "Авиастроение")
    private String name;
    /**
     * Признак активности индустрии.
     */
    @Schema(description = "Признак активности индустрии", example = "true", defaultValue = "true")
    @JsonProperty("is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

}
