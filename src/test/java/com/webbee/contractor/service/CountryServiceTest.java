package com.webbee.contractor.service;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.mapper.CountryMapper;
import com.webbee.contractor.model.Country;
import com.webbee.contractor.repository.CountryRepository;
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
    CountryRepository countryRepository;
    @Mock
    CountryMapper countryMapper;
    @InjectMocks
    CountryService countryService;

    CountryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test()
    @DisplayName("getAll() возвращает список всех стран")
    void getAll_returnsCountryDtoList() {
        Country country = new Country("ABH", "Абхазия", true);
        CountryDto countryDto = new CountryDto("ABH", "Абхазия", true);

        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.countryToCountryDto(country)).thenReturn(countryDto);

        List<CountryDto> countryDtoList = countryService.getAll();

        assertEquals(1, countryDtoList.size());
        assertEquals("ABH", countryDtoList.get(0).getId());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("save() сохраняет country")
    void save_callsRepositorySave() {
        CountryDto countryDto = new CountryDto("ABH", "Абхазия", true);
        Country country = new Country("ABH", "Абхазия", true);

        when(countryMapper.countryDtoToCountry(countryDto)).thenReturn(country);

        countryService.save(countryDto);

        verify(countryRepository).save(country);
    }

    @Test
    @DisplayName("delete() производит логическое удаление country")
    void delete_callsRepositoryDelete() {
        countryService.delete("ABH");
        verify(countryRepository).delete("ABH");
    }

    @Test
    @DisplayName("getById() возвращает country по Id")
    void getById_returnsCountryDto_whenExists() {
        CountryDto countryDto = new CountryDto("ABH", "Абхазия", true);
        Country country = new Country("ABH", "Абхазия", true);

        when(countryRepository.findById("ABH")).thenReturn(country);
        when(countryMapper.countryToCountryDto(country)).thenReturn(countryDto);

        CountryDto countryDtoResult = countryService.getById("ABH");

        assertNotNull(countryDtoResult);
        assertEquals("ABH", countryDtoResult.getId());
        verify(countryRepository).findById("ABH");
    }
}
