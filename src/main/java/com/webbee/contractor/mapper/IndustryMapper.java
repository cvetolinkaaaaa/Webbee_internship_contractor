package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.model.Industry;
import org.mapstruct.Mapper;

/**
 * MapStruct-маппер для преобразования между Industry и IndustryDto.
 */
@Mapper(componentModel = "spring")
public interface IndustryMapper {

    /**
     * Преобразует entity Industry в DTO.
     */
    IndustryDto industryToIndustryDto(Industry industry);

    /**
     * Преобразует DTO в entity Industry.
     */
    Industry industryDtoToIndustry(IndustryDto industryDto);

}
