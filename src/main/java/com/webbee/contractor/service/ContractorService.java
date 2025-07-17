package com.webbee.contractor.service;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorSearchRequest;
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
        return contractorRepository.findById(id)
                .map(contractorMapper::contractorToContractorDto)
                .orElse(null);
    }

    public void save(ContractorDto contractorDto) {
        Contractor contractor = contractorMapper.contractorDtoToContractor(contractorDto);
        contractorRepository.save(contractor);
    }

    public void delete(String id) {
        contractorRepository.delete(id);
    }

    public List<ContractorDto> search(ContractorSearchRequest contractorSearchRequest) {
        List<Contractor> found = contractorRepository.search(contractorSearchRequest);
        return found.stream()
                .map(contractorMapper::contractorToContractorDto)
                .toList();
    }

}
