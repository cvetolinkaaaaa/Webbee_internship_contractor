package com.webbee.contractor.service;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.mapper.OrgFormMapper;
import com.webbee.contractor.model.OrgForm;
import com.webbee.contractor.repository.OrgFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для бизнес-логики работы со организациями.
 */
@Service
public class OrgFormService {

    private final OrgFormRepository orgFormRepository;
    private final OrgFormMapper orgFormMapper;

    @Autowired
    public OrgFormService(OrgFormRepository orgFormRepository, OrgFormMapper orgFormMapper) {
        this.orgFormRepository = orgFormRepository;
        this.orgFormMapper = orgFormMapper;
    }

    /**
     * Возвращает список всех организаций.
     */
    public List<OrgFormDto> getAll() {
        return orgFormRepository.findAll().stream()
                .map(orgFormMapper::orgFormToOrgFormDto)
                .toList();
    }

    /**
     * Находит организацию по id и возвращает DTO.
     */
    public OrgFormDto getById(int id) {
        OrgForm orgForm = orgFormRepository.findById(id);
        return Objects.isNull(orgForm) ? null : orgFormMapper.orgFormToOrgFormDto(orgForm);
    }

    /**
     * Создаёт новую или обновляет существующую организацию.
     */
    public OrgFormDto save(OrgFormDto orgFormDto) {
        OrgForm orgForm = orgFormMapper.orgFormDtoToOrgForm(orgFormDto);
        OrgForm orgFormWithId = orgFormRepository.save(orgForm);
        return orgFormMapper.orgFormToOrgFormDto(orgFormWithId);    }

    /**
     * Логически удаляет организацию.
     */
    public void delete(int id) {
        orgFormRepository.delete(id);
    }

}
