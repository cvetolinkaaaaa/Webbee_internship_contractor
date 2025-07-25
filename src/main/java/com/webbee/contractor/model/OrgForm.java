package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity-класс, представляющий организацию в базе данных.
 * @author Evseeva Tsvetolina
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("org_form")
public class OrgForm {

    /**
     * Уникальный идентификатор организации.
     */
    @Id
    private Integer id;
    /**
     * Наименование организации.
     */
    private String name;
    /**
     * Признак активности (true — организация активна, false — удалена логически).
     */
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

}
