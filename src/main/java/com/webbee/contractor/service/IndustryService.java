package com.webbee.contractor.service;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.mapper.IndustryMapper;
import com.webbee.contractor.model.Industry;
import com.webbee.contractor.repository.IndustryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для бизнес-логики работы со индустриями.
 */
@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;
    private final IndustryMapper industryMapper;

    /**
     * Возвращает список всех индустрий.
     */
    public List<IndustryDto> getAll() {
        return industryRepository.findAll().stream()
                .map(industryMapper::industryToIndustryDto)
                .toList();
    }

    /**
     * Находит индустрию по id.
     */
    public IndustryDto getById(int id) {
        Industry industry = industryRepository.findById(id);
        return Objects.isNull(industry) ? null : industryMapper.industryToIndustryDto(industry);
    }

    /**
     * Создаёт новую или обновляет существующую индустрию.
     */
    public IndustryDto save(IndustryDto industryDto) {
        Industry industry = industryMapper.industryDtoToIndustry(industryDto);
        Industry industryWithId = industryRepository.save(industry);
        return industryMapper.industryToIndustryDto(industryWithId);
    }

    /**
     * Логически удаляет индустрию.
     */
    public void delete(int id) {
        industryRepository.delete(id);
    }

}
