package com.webbee.contractor.controller;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.service.IndustryService;
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
 * UI контроллер для управления справочником индустрий с проверкой ролей.
 */
@RestController
@RequestMapping("/ui/industry")
@Tag(name = "Industry UI", description = "Industry UI API с проверкой ролей")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class IndustryUIController {

    private final IndustryService industryService;

    /**
     * Получает список всех индустрий.
     */
    @Operation(summary = "Получить все индустрии",
               description = "Возвращает список всех активных индустрий")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список индустрий")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @GetMapping("/all")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<List<IndustryDto>> getAll() {

        return ResponseEntity.ok(industryService.getAll());
    }

    /**
     * Получает индустрию по id.
     */
    @Operation(summary = "Получить индустрию по id",
               description = "Возвращает индустрию по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Индустрия найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Индустрия не найдена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<IndustryDto> getById(@PathVariable int id) {

        IndustryDto industryDto = industryService.getById(id);
        if (industryDto != null) {
            return ResponseEntity.ok().body(industryDto);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Создаёт или обновляет индустрию.
     */
    @Operation(summary = "Сохранить индустрию",
               description = "Сохраняет индустрию или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Индустрия успешно создана/обновлена")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @PutMapping("/save")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity save(@RequestBody IndustryDto industryDto) {

        industryService.save(industryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * Логически удаляет индустрию по id.
     */
    @Operation(summary = "Удалить индустрию",
               description = "Производит логическое удаление индустрии")
    @ApiResponse(responseCode = "204", description = "Индустрия удалена (is_active=false)")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity delete(@PathVariable int id) {

        industryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
