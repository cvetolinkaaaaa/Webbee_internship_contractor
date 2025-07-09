package com.webbee.contractor.service;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.mapper.OrgFormMapper;
import com.webbee.contractor.model.OrgForm;
import com.webbee.contractor.repository.OrgFormRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrgFormServiceTest {

    @Mock
    OrgFormRepository orgFormRepository;
    @Mock
    OrgFormMapper orgFormMapper;
    @InjectMocks
    OrgFormService orgFormService;

    OrgFormServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test()
    @DisplayName("getAll() возвращает список всех организаций")
    void getAll_returnsOrgFormDtoList() {
        OrgForm orgForm = new OrgForm(1, "Организация",true);
        OrgFormDto orgFormDto = new OrgFormDto(1, "Организация",true);

        when(orgFormRepository.findAll()).thenReturn(List.of(orgForm));
        when(orgFormMapper.orgFormToOrgFormDto(orgForm)).thenReturn(orgFormDto);

        List<OrgFormDto> orgFormDtoList = orgFormService.getAll();

        assertEquals(1, orgFormDtoList.size());
        assertEquals("Организация", orgFormDtoList.get(0).getName());
        verify(orgFormRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("save() сохраняет организацию")
    void save_callsRepositorySave() {
        OrgForm orgForm = new OrgForm(1, "Организация",true);
        OrgFormDto orgFormDto = new OrgFormDto(1, "Организация",true);

        when(orgFormMapper.orgFormDtoToOrgForm(orgFormDto)).thenReturn(orgForm);
        when(orgFormRepository.save(orgForm)).thenReturn(orgForm);
        when(orgFormMapper.orgFormToOrgFormDto(orgForm)).thenReturn(orgFormDto);

        OrgFormDto orgFormDtoSaved = orgFormService.save(orgFormDto);

        verify(orgFormRepository).save(orgForm);
        assertNotNull(orgFormDtoSaved);
        assertEquals("Организация", orgFormDtoSaved.getName());
    }

    @Test
    @DisplayName("delete() производит логическое удаление организации")
    void delete_callsRepositoryDelete() {
        orgFormService.delete(1);
        verify(orgFormRepository).delete(1);
    }

    @Test
    @DisplayName("getById() возвращает организацию по Id")
    void getById_returnsOrgFormDto_whenExists() {
        OrgForm orgForm = new OrgForm(1, "Организация", true);
        OrgFormDto orgFormDto = new OrgFormDto(1, "Организация", true);

        when(orgFormRepository.findById(1)).thenReturn(orgForm);
        when(orgFormMapper.orgFormToOrgFormDto(orgForm)).thenReturn(orgFormDto);

        OrgFormDto orgFormDtoResult = orgFormService.getById(1);

        assertNotNull(orgFormDtoResult);
        assertEquals("Организация", orgFormDtoResult.getName());
        verify(orgFormRepository).findById(1);
    }
}