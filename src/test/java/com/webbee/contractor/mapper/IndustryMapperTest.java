package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.model.Industry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class IndustryMapperTest {

    private final IndustryMapper industryMapper = Mappers.getMapper(IndustryMapper.class);

    @Test
    @DisplayName("Маппит Industry в IndustryDto")
    void industryToIndustryDto_mapsAllFields() {
        Industry industry = new Industry();
        industry.setId(1);
        industry.setName("IT");
        industry.setIsActive(true);

        IndustryDto industryDto = industryMapper.industryToIndustryDto(industry);

        assertNotNull(industryDto);
        assertEquals(1, industryDto.getId());
        assertEquals("IT", industryDto.getName());
        assertTrue(industryDto.getIsActive());
    }

    @Test
    @DisplayName("Маппит IndustryDto в Industry")
    void industryDtoToIndustry_mapsAllFields() {
        IndustryDto industryDto = new IndustryDto();
        industryDto.setId(2);
        industryDto.setName("Промышленность");
        industryDto.setIsActive(false);

        Industry industry = industryMapper.industryDtoToIndustry(industryDto);

        assertNotNull(industry);
        assertEquals(2, industry.getId());
        assertEquals("Промышленность", industry.getName());
        assertFalse(industry.getIsActive());
    }
}
