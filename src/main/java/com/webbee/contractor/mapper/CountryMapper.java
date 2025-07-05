package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.model.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDto countryToCountryDto(Country entity);

    Country countryDtoToCountry(CountryDto dto);

}
