package com.webbee.contractor.service;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.mapper.IndustryMapper;
import com.webbee.contractor.model.Industry;
import com.webbee.contractor.repository.IndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class IndustryService {

    private final IndustryRepository industryRepository;
    private final IndustryMapper industryMapper;

    @Autowired
    public IndustryService(IndustryRepository industryRepository, IndustryMapper industryMapper) {
        this.industryRepository = industryRepository;
        this.industryMapper = industryMapper;
    }

    public List<IndustryDto> getAll() {
        return industryRepository.findAll().stream()
                .map(industryMapper::industryToIndustryDto)
                .toList();
    }

    public IndustryDto getById(int id) {
        Industry industry = industryRepository.findById(id);
        return Objects.isNull(industry) ? null : industryMapper.industryToIndustryDto(industry);
    }

    public void save(IndustryDto industryDto) {
        industryRepository.save(industryMapper.industryDtoToIndustry(industryDto));
    }

    public void delete(int id) {
        industryRepository.delete(id);
    }

}
