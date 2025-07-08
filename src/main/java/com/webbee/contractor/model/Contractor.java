package com.webbee.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("contractor")
public class Contractor {

    @Id
    private String id;
    private String parentId;
    private String name;
    private String nameFull;
    private String inn;
    private String ogrn;
    private String country;
    private Integer industry;
    private Integer orgForm;
    private java.time.LocalDateTime createDate;
    private java.time.LocalDateTime modifyDate;
    private String createUserId;
    private String modifyUserId;
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

}
