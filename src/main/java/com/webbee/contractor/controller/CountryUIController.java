package com.webbee.contractor.controller;

import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UI контроллер для управления справочником стран с проверкой ролей.
 * @author Evseeva Tsvetolina
 */
@RestController
@RequestMapping("/ui/country")
@Tag(name = "Country UI", description = "Country UI API с проверкой ролей")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CountryUIController {

    private final CountryService countryService;

    /**
     * Получает список всех стран.
     */
    @Operation(summary = "Получить все страны",
               description = "Возвращает список всех активных стран")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список стран")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @GetMapping("/all")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<List<CountryDto>> getAll() {

        return ResponseEntity.ok(countryService.getAll());

    }

    /**
     * Получает страну по id.
     */
    @Operation(summary = "Получить страну по id",
               description = "Возвращает страну по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Страна найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Страна не найдена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<CountryDto> getById(@PathVariable String id) {

        CountryDto countryDto = countryService.getById(id);
        if (countryDto != null) {
            return ResponseEntity.ok(countryDto);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Создаёт или обновляет страну.
     */
    @Operation(summary = "Сохранить страну",
               description = "Сохраняет страну или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Страна успешно создана/обновлена")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @PutMapping("/save")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity<?> save(@RequestBody CountryDto countryDto) {

        countryService.save(countryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * Логически удаляет страну по id.
     */
    @Operation(summary = "Удалить страну",
               description = "Производит логическое удаление страны")
    @ApiResponse(responseCode = "204", description = "Страна удалена (is_active=false)")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity delete(@PathVariable String id) {

        countryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
