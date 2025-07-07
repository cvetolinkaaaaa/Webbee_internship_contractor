package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.model.OrgForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrgFormMapper {

    OrgFormDto orgFormToOrgFormDto(OrgForm orgForm);

    OrgForm orgFormDtoToOrgForm(OrgFormDto orgFormDto);

}
