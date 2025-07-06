package com.webbee.contractor.controller;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.service.OrgFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/org_form")
public class OrgFormController {

    private final OrgFormService orgFormService;

    @Autowired
    public OrgFormController(OrgFormService orgFormService) {
        this.orgFormService = orgFormService;
    }

    @GetMapping("/all")
    public List<OrgFormDto> getAll() {
        return orgFormService.getAll();
    }

    @GetMapping("/{id}")
    public OrgFormDto getById(@PathVariable int id) {
        return orgFormService.getById(id);
    }

    @PutMapping("/save")
    public void save(@RequestBody OrgFormDto orgFormDto) {
        orgFormService.save(orgFormDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        orgFormService.delete(id);
    }

}
