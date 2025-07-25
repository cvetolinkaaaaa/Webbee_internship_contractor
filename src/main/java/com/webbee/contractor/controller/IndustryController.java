package com.webbee.contractor.controller;

import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.service.IndustryService;
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
 * REST-контроллер для управления справочником индустрий.
 * @author Evseeva Tsvetolina
 */
@RestController
@RequestMapping("/industry")
@Tag(name = "Industry", description = "Industry API")
public class IndustryController {

    private final IndustryService industryService;

    @Autowired
    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    /**
     * Получает список всех индустрий.
     */
    @Operation(summary = "Получить все индустрии", description = "Возвращает список всех индустрий")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список индустрий")
    @GetMapping("/all")
    public ResponseEntity<List<IndustryDto>> getAll() {
        return ResponseEntity.ok(industryService.getAll());
    }

    /**
     * Получает индустрию по id.
     */
    @Operation(summary = "Получить индустрию по id", description = "Возвращает индустрию по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Индустрия найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Индустрия не найдена"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<IndustryDto> getById(@PathVariable int id) {
        IndustryDto industryDto = industryService.getById(id);
        if (industryDto != null) {
            return ResponseEntity.ok().body(industryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создает или обновляет индустрию.
     */
    @Operation(summary = "Сохранить индустрию", description = "Сохраняет индустрию или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Индустрия успешно создана/обновлена")
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody IndustryDto industryDto) {
        IndustryDto industryDtoSaved = industryService.save(industryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(industryDtoSaved);
    }

    /**
     * Логически удаляет индустрию по id.
     */
    @Operation(summary = "Удалить индустрию", description = "Производит логическое удаление индустрии")
    @ApiResponse(responseCode = "204", description = "Индустрия удалена (is_active=false)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        industryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
