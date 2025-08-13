package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity-класс, представляющий страну в базе данных.
 * @author Evseeva Tsvetolina
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Country {

    /**
     * Уникальный идентификатор страны.
     */
    private String id;
    /**
     * Наименование страны.
     */
    private String name;
    /**
     * Признак активности (true — страна активна, false — удалена логически).
     */
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

}
