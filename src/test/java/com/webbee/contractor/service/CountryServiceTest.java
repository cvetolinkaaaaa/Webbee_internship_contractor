package com.webbee.contractor.service;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.mapper.IndustryMapper;
import com.webbee.contractor.model.Industry;
import com.webbee.contractor.repository.IndustryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CountryServiceTest {

    @Mock
    IndustryRepository industryRepository;
    @Mock
    IndustryMapper industryMapper;
    @InjectMocks
    IndustryService industryService;

    CountryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test()
    @DisplayName("getAll() возвращает список всех индустрий")
    void getAll_returnsCountryDtoList() {
        Industry industry = new Industry(1, "Авиастроение", true);
        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);

        when(industryRepository.findAll()).thenReturn(List.of(industry));
        when(industryMapper.industryToIndustryDto(industry)).thenReturn(industryDto);

        List<IndustryDto> industryDtoList = industryService.getAll();

        assertEquals(1, industryDtoList.size());
        assertEquals("Авиастроение", industryDtoList.get(0).getName());
        verify(industryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("save() сохраняет индустрию")
    void save_callsRepositorySave() {
        Industry industry = new Industry(1, "Авиастроение", true);
        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);

        when(industryMapper.industryDtoToIndustry(industryDto)).thenReturn(industry);
        when(industryRepository.save(industry)).thenReturn(industry);
        when(industryMapper.industryToIndustryDto(industry)).thenReturn(industryDto);

        IndustryDto industryDtoSaved = industryService.save(industryDto);

        verify(industryRepository).save(industry);
        assertNotNull(industryDtoSaved);
        assertEquals("Авиастроение", industryDtoSaved.getName());
    }

    @Test
    @DisplayName("delete() производит логическое удаление индустрии")
    void delete_callsRepositoryDelete() {
        industryService.delete(1);
        verify(industryRepository).delete(1);
    }

    @Test
    @DisplayName("getById() возвращает индустрию по Id")
    void getById_returnsCountryDto_whenExists() {
        Industry industry = new Industry(1, "Авиастроение", true);
        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);

        when(industryRepository.findById(1)).thenReturn(industry);
        when(industryMapper.industryToIndustryDto(industry)).thenReturn(industryDto);

        IndustryDto industryDtoResult = industryService.getById(1);

        assertNotNull(industryDtoResult);
        assertEquals("Авиастроение", industryDtoResult.getName());
        verify(industryRepository).findById(1);
    }
}