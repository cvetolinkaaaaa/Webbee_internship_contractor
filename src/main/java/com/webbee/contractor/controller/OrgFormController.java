package com.webbee.contractor.controller;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.service.OrgFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrgFormDto>> getAll() {
        return ResponseEntity.ok(orgFormService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgFormDto> getById(@PathVariable int id) {
        OrgFormDto orgFormDto = orgFormService.getById(id);
        if (orgFormDto == null) {
            return ResponseEntity.ok().body(orgFormDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrgFormDto orgFormDto) {
        OrgFormDto orgFormDtoSaved = orgFormService.save(orgFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orgFormDtoSaved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        orgFormService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
