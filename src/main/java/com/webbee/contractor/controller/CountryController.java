package com.webbee.contractor.controller;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * REST-контроллер для управления справочником стран.
 */
@RestController
@RequestMapping("/country")
@Tag(name = "Country", description = "Country API")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Получает список всех стран.
     */
    @Operation(summary = "Получить все страны", description = "Возвращает список всех активных стран")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список стран")
    @GetMapping("/all")
    public ResponseEntity<List<CountryDto>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }

    /**
     * Получает страну по id.
     */
    @Operation(summary = "Получить страну по id", description = "Возвращает страну по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Страна найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Страна не найдена"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getById(@PathVariable String id) {
        CountryDto countryDto = countryService.getById(id);
        if (countryDto == null) {
            return ResponseEntity.ok(countryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создает или обновляет страну.
     */
    @Operation(summary = "Сохранить страну", description = "Сохраняет страну или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Страна успешно создана/обновлена")
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody CountryDto countryDto) {
        countryService.save(countryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Логически удаляет страну по id.
     */
    @Operation(summary = "Удалить страну", description = "Производит логическое удаление страны")
    @ApiResponse(responseCode = "204", description = "Страна удалена (is_active=false)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        countryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
