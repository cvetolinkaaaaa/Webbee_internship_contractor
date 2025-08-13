package com.webbee.contractor.service;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.mapper.CountryMapper;
import com.webbee.contractor.repository.CountryRepository;
import com.webbee.contractor.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для бизнес-логики работы со странами.
 * @author Evseeva Tsvetolina
 */
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    /**
     * Возвращает список всех стран.
     */
    public List<CountryDto> getAll() {
        return countryRepository.findAll().stream()
                .map(countryMapper::countryToCountryDto)
                .toList();
    }

    /**
     * Находит страну по id и возвращает DTO.
     */
    public CountryDto getById(String id) {
        Country country = countryRepository.findById(id);
        return Objects.isNull(country) ? null : countryMapper.countryToCountryDto(country);
    }

    /**
     * Создаёт новую или обновляет существующую страну.
     */
    public void save(CountryDto countryDto) {
        countryRepository.save(countryMapper.countryDtoToCountry(countryDto));
    }

    /**
     * Логически удаляет страну.
     */
    public void delete(String id) {
        countryRepository.delete(id);
    }

}
