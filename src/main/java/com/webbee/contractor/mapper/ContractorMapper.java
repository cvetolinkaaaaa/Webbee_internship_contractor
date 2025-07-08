package com.webbee.contractor.mapper;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.model.Contractor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractorMapper {

    ContractorDto contractorToContractorDto(Contractor contractor);
    Contractor contractorDtoToContractor(ContractorDto contractorDto);

}
