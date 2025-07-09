package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.model.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CountryMapperTest {

    private final CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    @Test
    @DisplayName("Маппит Country в CountryDto")
    void countryToCountryDto_mapsAllFields() {
        Country country = new Country();
        country.setId("RUS");
        country.setName("Россия");
        country.setIsActive(true);

        CountryDto countryDto = countryMapper.countryToCountryDto(country);

        assertNotNull(countryDto);
        assertEquals("RUS", countryDto.getId());
        assertEquals("Россия", countryDto.getName());
        assertTrue(countryDto.getIsActive());
    }

    @Test
    @DisplayName("Маппит CountryDto в Country")
    void countryDtoToCountry_mapsAllFields() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId("KAZ");
        countryDto.setName("Казахстан");
        countryDto.setIsActive(false);

        Country country = countryMapper.countryDtoToCountry(countryDto);

        assertNotNull(country);
        assertEquals("KAZ", country.getId());
        assertEquals("Казахстан", country.getName());
        assertFalse(country.getIsActive());
    }
}
