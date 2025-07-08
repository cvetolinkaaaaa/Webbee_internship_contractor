package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity-класс, представляющий контрагента в базе данных.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("contractor")
public class Contractor {

    /** Уникальный Id контрагента. */
    @Id
    private String id;
    /** Id родительского контрагента. */
    private String parentId;
    /** Краткое наименование. */
    private String name;
    /** Полное наименование. */
    private String nameFull;
    /** ИНН. */
    private String inn;
    /** ОГРН. */
    private String ogrn;
    /** Идентификатор страны. */
    private String country;
    /** Идентификатор индустрии. */
    private Integer industry;
    /** Идентификатор организации. */
    private Integer orgForm;
    /** Дата создания записи. */
    private java.time.LocalDateTime createDate;
    /** Дата последнего изменения записи. */
    private java.time.LocalDateTime modifyDate;
    /** Id пользователя, создавшего запись. */
    private String createUserId;
    /** Id пользователя, последнего изменившего запись. */
    private String modifyUserId;
    /** Признак активности (true — активен, false — удалён). */
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

}
