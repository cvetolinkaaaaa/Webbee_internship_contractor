package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.model.Contractor;
import org.mapstruct.Mapper;

/**
 * MapStruct-маппер для преобразования между Contractor и ContractorDto.
 * @author Evseeva Tsvetolina
 */
@Mapper(componentModel = "spring")
public interface ContractorMapper {

    /**
     * Преобразует entity Contractor в DTO.
     */
    ContractorDto contractorToContractorDto(Contractor contractor);

    /**
     * Преобразует DTO в entity Contractor.
     */
    Contractor contractorDtoToContractor(ContractorDto contractorDto);

}
