package com.webbee.contractor.controller;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.service.IndustryService;
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
@RequestMapping("/industry")
public class IndustryController {

    private final IndustryService industryService;

    @Autowired
    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @GetMapping("/all")
    public List<IndustryDto> getAll() {
        return industryService.getAll();
    }

    @GetMapping("/{id}")
    public IndustryDto getById(@PathVariable int id) {
        return industryService.getById(id);
    }

    @PutMapping("/save")
    public void save(@RequestBody IndustryDto industryDto) {
        industryService.save(industryDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        industryService.delete(id);
    }

}