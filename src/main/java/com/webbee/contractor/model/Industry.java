package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity-класс, представляющий индустрию в базе данных.
 * @author Evseeva Tsvetolina
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("industry")
public class Industry {

    /**
     * Уникальный идентификатор индустрии.
     */
    @Id
    private Integer id;
    /**
     * Наименование индустрии.
     */
    private String name;
    /**
     * Признак активности (true — индустрия активна, false — удалена логически).
     */
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

}
