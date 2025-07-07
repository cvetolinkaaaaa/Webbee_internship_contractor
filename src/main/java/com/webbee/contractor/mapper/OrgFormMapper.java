package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.model.OrgForm;
import org.mapstruct.Mapper;

/**
 * MapStruct-маппер для преобразования между OrgForm и OrgFormDto.
 */
@Mapper(componentModel = "spring")
public interface OrgFormMapper {

    /**
     * Преобразует entity OrgForm в DTO.
     */
    OrgFormDto orgFormToOrgFormDto(OrgForm orgForm);

    /**
     * Преобразует DTO в entity OrgForm.
     */
    OrgForm orgFormDtoToOrgForm(OrgFormDto orgFormDto);

}
