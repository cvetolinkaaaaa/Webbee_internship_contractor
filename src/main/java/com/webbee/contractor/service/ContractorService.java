package com.webbee.contractor.service;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.mapper.ContractorMapper;
import com.webbee.contractor.model.Contractor;
import com.webbee.contractor.repository.ContractorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ContractorMapper contractorMapper;

    public ContractorService(ContractorRepository contractorRepository, ContractorMapper contractorMapper) {
        this.contractorRepository = contractorRepository;
        this.contractorMapper = contractorMapper;
    }

    public List<ContractorDto> getAll() {
        return contractorRepository.findAll().stream()
                .map(contractorMapper::contractorToContractorDto)
                .toList();
    }

    public ContractorDto getById(String id) {
        Contractor contractor = contractorRepository.findById(id);
        return Objects.isNull(contractor) ? null : contractorMapper.contractorToContractorDto(contractor);
    }

    public void save(ContractorDto contractorDto) {
        Contractor contractor = contractorMapper.contractorDtoToContractor(contractorDto);
        contractorRepository.save(contractor);
    }

    public void delete(String id) {
        contractorRepository.delete(id);
    }

}
