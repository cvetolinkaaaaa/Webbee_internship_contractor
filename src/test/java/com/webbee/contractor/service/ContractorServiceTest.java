package com.webbee.contractor.service;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.mapper.ContractorMapper;
import com.webbee.contractor.model.Contractor;
import com.webbee.contractor.repository.ContractorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ContractorServiceTest {

    ContractorRepository contractorRepository;
    ContractorMapper contractorMapper;
    ContractorService contractorService;

    @BeforeEach
    void setUp() {
        contractorRepository = mock(ContractorRepository.class);
        contractorMapper = mock(ContractorMapper.class);
        contractorService = new ContractorService(contractorRepository, contractorMapper);
    }

    @Test
    @DisplayName("getAll() возвращает список контрагентов")
    void getAll() {
        Contractor contractor = new Contractor();
        contractor.setId("123");
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("123");

        when(contractorRepository.findAll()).thenReturn(Arrays.asList(contractor));
        when(contractorMapper.contractorToContractorDto(contractor)).thenReturn(contractorDto);

        List<ContractorDto> contractorDtoResult = contractorService.getAll();
        assertEquals(1, contractorDtoResult.size());
        assertEquals("123", contractorDtoResult.get(0).getId());
    }

    @Test
    @DisplayName("getById() возвращает контрагента по Id")
    void getById() {
        Contractor contractor = new Contractor();
        contractor.setId("321");
        ContractorDto contractorDto = new ContractorDto();
        contractorDto.setId("321");

        when(contractorRepository.findById("321")).thenReturn(Optional.of(contractor));
        when(contractorMapper.contractorToContractorDto(contractor)).thenReturn(contractorDto);

        ContractorDto contractorDtoResult = contractorService.getById("321");
        assertNotNull(contractorDtoResult);
        assertEquals("321", contractorDtoResult.getId());
    }


    @Test
    @DisplayName("save() сохраняет контрагента")
    void save() {
        ContractorDto contractorDto = new ContractorDto();
        Contractor contractor = new Contractor();

        when(contractorMapper.contractorDtoToContractor(contractorDto)).thenReturn(contractor);

        contractorService.save(contractorDto);

        verify(contractorRepository).save(contractor);
    }

    @Test
    @DisplayName("delete() логически удаляет контрагента")
    void delete() {
        contractorService.delete("777");
        verify(contractorRepository).delete("777");
    }

}
