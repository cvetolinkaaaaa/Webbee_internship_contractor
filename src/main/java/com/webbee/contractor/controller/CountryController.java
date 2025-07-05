package com.webbee.contractor.controller;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.service.CountryService;
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
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<CountryDto> getAll() {
        return countryService.getAll();
    }

    @GetMapping("/{id}")
    public CountryDto getById(@PathVariable String id) {
        return countryService.getById(id);
    }

    @PutMapping("/save")
    public void save(@RequestBody CountryDto countryDto) {
        countryService.save(countryDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        countryService.delete(id);
    }

}
