package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.model.Contractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ContractorMapperTest {

    private final ContractorMapper mapper = Mappers.getMapper(ContractorMapper.class);

    @Test
    @DisplayName("Маппит Contractor в ContractorDto")
    void contractorToContractorDto_mapsAllFields() {
        Contractor contractor = new Contractor();
        contractor.setId("CNT123");
        contractor.setParentId("PARENT1");
        contractor.setNameFull("ООО Рога и Копыта");
        contractor.setInn("7701234567");
        contractor.setOgrn("1027700132195");
        contractor.setCountry("RUS");
        contractor.setIndustry(1);
        contractor.setOrgForm(2);

        ContractorDto dto = mapper.contractorToContractorDto(contractor);

        assertNotNull(dto);
        assertEquals("CNT123", dto.getId());
        assertEquals("PARENT1", dto.getParentId());
        assertEquals("ООО Рога и Копыта", dto.getNameFull());
        assertEquals("7701234567", dto.getInn());
        assertEquals("1027700132195", dto.getOgrn());
        assertEquals("RUS", dto.getCountry());
        assertEquals(1, dto.getIndustry());
        assertEquals(2, dto.getOrgForm());
    }

    @Test
    @DisplayName("Маппит ContractorDto в Contractor")
    void contractorDtoToContractor_mapsAllFields() {
        ContractorDto dto = new ContractorDto();
        dto.setId("CNT124");
        dto.setParentId("PARENT2");
        dto.setNameFull("АО Пример");
        dto.setInn("1112223334");
        dto.setOgrn("1027700132299");
        dto.setCountry("KAZ");
        dto.setIndustry(3);
        dto.setOrgForm(4);

        Contractor contractor = mapper.contractorDtoToContractor(dto);

        assertNotNull(contractor);
        assertEquals("CNT124", contractor.getId());
        assertEquals("PARENT2", contractor.getParentId());
        assertEquals("АО Пример", contractor.getNameFull());
        assertEquals("1112223334", contractor.getInn());
        assertEquals("1027700132299", contractor.getOgrn());
        assertEquals("KAZ", contractor.getCountry());
        assertEquals(3, contractor.getIndustry());
        assertEquals(4, contractor.getOrgForm());
    }
}
