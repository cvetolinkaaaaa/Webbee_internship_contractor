package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.model.Country;
import org.mapstruct.Mapper;

/**
 * MapStruct-маппер для преобразования между Country и CountryDto.
 * @author Evseeva Tsvetolina
 */
@Mapper(componentModel = "spring")
public interface CountryMapper {

    /**
     * Преобразует entity Country в DTO.
     */
    CountryDto countryToCountryDto(Country entity);

    /**
     * Преобразует DTO в entity Country.
     */
    Country countryDtoToCountry(CountryDto dto);

}
