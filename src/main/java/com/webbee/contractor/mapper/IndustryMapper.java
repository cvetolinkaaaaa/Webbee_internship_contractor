package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.model.Industry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndustryMapper {

    IndustryDto industryToIndustryDto(Industry industry);

    Industry industryDtoToIndustry(IndustryDto industryDto);

}