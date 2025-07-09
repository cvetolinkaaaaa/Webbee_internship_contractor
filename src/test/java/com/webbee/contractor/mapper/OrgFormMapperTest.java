package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.model.OrgForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class OrgFormMapperTest {

    private final OrgFormMapper orgFormMapper = Mappers.getMapper(OrgFormMapper.class);

    @Test
    @DisplayName("Маппит OrgForm в OrgFormDto")
    void orgFormToOrgFormDto_mapsAllFields() {
        OrgForm orgForm = new OrgForm();
        orgForm.setId(1);
        orgForm.setName("ООО");
        orgForm.setIsActive(true);

        OrgFormDto orgFormDto = orgFormMapper.orgFormToOrgFormDto(orgForm);

        assertNotNull(orgFormDto);
        assertEquals(1, orgFormDto.getId());
        assertEquals("ООО", orgFormDto.getName());
        assertTrue(orgFormDto.getIsActive());
    }

    @Test
    @DisplayName("Маппит OrgFormDto в OrgForm")
    void orgFormDtoToOrgForm_mapsAllFields() {
        OrgFormDto orgFormDto = new OrgFormDto();
        orgFormDto.setId(2);
        orgFormDto.setName("АО");
        orgFormDto.setIsActive(false);

        OrgForm orgForm = orgFormMapper.orgFormDtoToOrgForm(orgFormDto);

        assertNotNull(orgForm);
        assertEquals(2, orgForm.getId());
        assertEquals("АО", orgForm.getName());
        assertFalse(orgForm.getIsActive());
    }
}
