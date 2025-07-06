package com.webbee.contractor.controller;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Country", description = "Country API")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все страны", description = "Возвращает список всех активных стран")
    public List<CountryDto> getAll() {
        return countryService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить страну по id", description = "Возвращает страну по указанному id")
    public CountryDto getById(@PathVariable String id) {
        return countryService.getById(id);
    }

    @PutMapping("/save")
    @Operation(summary = "Сохранить страну", description = "Сохраняет страну или изменяет существующую")
    public void save(@RequestBody CountryDto countryDto) {
        countryService.save(countryDto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить страну", description = "Производит логическое удаление страны")
    public void delete(@PathVariable String id) {
        countryService.delete(id);
    }

}
