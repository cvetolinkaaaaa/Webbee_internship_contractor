package com.webbee.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContractorDto {

    private String id;
    private String parentId;
    private String name;
    private String nameFull;
    private String inn;
    private String ogrn;
    private String country;
    private Integer industry;
    private Integer orgForm;
    private Boolean isActive;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private String createUserName;
    private String modifyUserName;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
