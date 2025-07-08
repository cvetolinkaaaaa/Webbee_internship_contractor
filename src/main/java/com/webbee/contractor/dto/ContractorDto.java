package com.webbee.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * DTO для передачи данных о контрагенте через REST API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContractorDto {

    /** Уникальный Id контрагента. */
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
    /** Id страны. */
    private String country;
    /** Id индустрии. */
    private Integer industry;
    /** Id организации. */
    private Integer orgForm;
    /** Признак активности (true — активен). */
    private Boolean isActive;
    /** Дата создания записи. */
    private LocalDateTime createTime;
    /** Дата последнего изменения записи. */
    private LocalDateTime modifyTime;
    /** Id пользователя, создавшего запись. */
    private String createUserName;
    /** ID пользователя, последнего изменившего запись. */
    private String modifyUserName;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
