package com.webbee.contractor.controller;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.service.OrgFormService;
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
 * REST-контроллер для управления справочником организаций.
 */
@RestController
@RequestMapping("/org_form")
@Tag(name = "OrgForm", description = "OrgForm API")
public class OrgFormController {

    private final OrgFormService orgFormService;

    @Autowired
    public OrgFormController(OrgFormService orgFormService) {
        this.orgFormService = orgFormService;
    }

    /**
     * Получает список всех организаций.
     */
    @Operation(summary = "Получить все организации", description = "Возвращает список всех организаций")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список организаций")
    @GetMapping("/all")
    public ResponseEntity<List<OrgFormDto>> getAll() {
        return ResponseEntity.ok(orgFormService.getAll());
    }

    /**
     * Получает организацию по id.
     */
    @Operation(summary = "Получить организацию по id", description = "Возвращает организацию по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Организация найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Организация не найдена"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrgFormDto> getById(@PathVariable int id) {
        OrgFormDto orgFormDto = orgFormService.getById(id);
        if (orgFormDto == null) {
            return ResponseEntity.ok().body(orgFormDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создает или обновляет организацию.
     */
    @Operation(summary = "Сохранить организацию", description = "Сохраняет организацию или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Организация успешно создана/обновлена")
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrgFormDto orgFormDto) {
        OrgFormDto orgFormDtoSaved = orgFormService.save(orgFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orgFormDtoSaved);
    }

    /**
     * Логически удаляет организацию по id.
     */
    @Operation(summary = "Удалить организацию", description = "Производит логическое удаление организации")
    @ApiResponse(responseCode = "204", description = "Организация удалена (is_active=false)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        orgFormService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
