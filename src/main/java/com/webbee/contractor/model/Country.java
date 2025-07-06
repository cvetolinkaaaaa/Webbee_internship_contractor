package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Entity-класс, представляющий страну в базе данных.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
