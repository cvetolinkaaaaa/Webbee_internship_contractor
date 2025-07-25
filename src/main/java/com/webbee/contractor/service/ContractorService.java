
package com.webbee.contractor.service;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.mapper.ContractorMapper;
import com.webbee.contractor.model.Contractor;
import com.webbee.contractor.repository.ContractorRepository;
import com.webbee.contractor.security.service.AuthorizationService;
import com.webbee.contractor.utils.UserIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для бизнес-логики работы с контрагентами.
 * @author Evseeva Tsvetolina
 */
@Service
@RequiredArgsConstructor
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ContractorMapper contractorMapper;
    private final AuthorizationService authorizationService;
    private final UserIdService userIdService;

    /**
     * Возвращает список всех контрагентов.
     */
    public List<ContractorDto> getAll() {
        return contractorRepository.findAll().stream()
                .map(contractorMapper::contractorToContractorDto)
                .toList();
    }

    /**
     * Находит контрагента по id.
     */
    public ContractorDto getById(String id) {
        return contractorRepository.findById(id)
                .map(contractorMapper::contractorToContractorDto)
                .orElse(null);
    }

    /**
     * Создаёт нового или обновляет существующего контрагента.
     */
    public void save(ContractorDto contractorDto) {
        Contractor contractor = contractorMapper.contractorDtoToContractor(contractorDto);
        contractorRepository.save(contractor);
    }

    /**
     * Логически удаляет контрагента.
     */
    public void delete(String id) {
        contractorRepository.delete(id);
    }

    /**
     * Выполняет поиск контрагентов.
     */
    public List<ContractorDto> search(ContractorSearchRequest contractorSearchRequest) {
        List<Contractor> found = contractorRepository.search(contractorSearchRequest);
        return found.stream()
                .map(contractorMapper::contractorToContractorDto)
                .toList();
    }

    /**
     * Создаёт нового или обновляет существующего контрагента.
     */
    public void saveWithAuth(ContractorDto contractorDto) {

        String userId = userIdService.getCurrentUserId();
        if (Objects.isNull(getById(contractorDto.getId()))) {
            contractorDto.setCreateUserId(userId);
        }
        contractorDto.setModifyUserId(userId);
        save(contractorDto);

    }

    /**
     * Выполняет поиск контрагентов с учетом ролевых ограничений.
     */
    public List<ContractorDto> searchWithAuth(ContractorSearchRequest contractorSearchRequest) {

        ContractorSearchRequest filteredRequest = authorizationService.applyContractorAccessFilter(contractorSearchRequest);
        return search(filteredRequest);

    }

}
