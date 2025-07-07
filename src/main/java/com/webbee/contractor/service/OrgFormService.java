package com.webbee.contractor.service;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.mapper.OrgFormMapper;
import com.webbee.contractor.model.OrgForm;
import com.webbee.contractor.repository.OrgFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrgFormService {

    private final OrgFormRepository orgFormRepository;
    private final OrgFormMapper orgFormMapper;

    @Autowired
    public OrgFormService(OrgFormRepository orgFormRepository, OrgFormMapper orgFormMapper) {
        this.orgFormRepository = orgFormRepository;
        this.orgFormMapper = orgFormMapper;
    }

    public List<OrgFormDto> getAll() {
        return orgFormRepository.findAll().stream()
                .map(orgFormMapper::orgFormToOrgFormDto)
                .toList();
    }

    public OrgFormDto getById(int id) {
        OrgForm orgForm = orgFormRepository.findById(id);
        return Objects.isNull(orgForm) ? null : orgFormMapper.orgFormToOrgFormDto(orgForm);
    }

    public OrgFormDto save(OrgFormDto orgFormDto) {
        OrgForm orgForm = orgFormMapper.orgFormDtoToOrgForm(orgFormDto);
        OrgForm orgFormWithId = orgFormRepository.save(orgForm);
        return orgFormMapper.orgFormToOrgFormDto(orgFormWithId);    }

    public void delete(int id) {
        orgFormRepository.delete(id);
    }

}
