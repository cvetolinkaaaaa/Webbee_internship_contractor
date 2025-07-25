package com.webbee.contractor.controller;

import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.service.OrgFormService;
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
 * UI контроллер для управления справочником организационных форм с проверкой ролей.
 * @author Evseeva Tsvetolina
 */
@RestController
@RequestMapping("/ui/org_form")
@Tag(name = "OrgForm UI", description = "OrgForm UI API с проверкой ролей")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrgFormUIController {

    private final OrgFormService orgFormService;

    /**
     * Получает список всех организационных форм.
     */
    @Operation(summary = "Получить все организационные формы",
               description = "Возвращает список всех активных организационных форм")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список организационных форм")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @GetMapping("/all")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<List<OrgFormDto>> getAll() {

        return ResponseEntity.ok(orgFormService.getAll());

    }

    /**
     * Получает организационную форму по id.
     */
    @Operation(summary = "Получить организационную форму по id",
               description = "Возвращает организационную форму по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Организационная форма найдена и возвращена"),
            @ApiResponse(responseCode = "404", description = "Организационная форма не найдена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<OrgFormDto> getById(@PathVariable int id) {

        OrgFormDto orgFormDto = orgFormService.getById(id);
        if (orgFormDto != null) {
            return ResponseEntity.ok().body(orgFormDto);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Создаёт или обновляет организационную форму.
     */
    @Operation(summary = "Сохранить организационную форму",
               description = "Сохраняет организационную форму или изменяет существующую")
    @ApiResponse(responseCode = "201", description = "Организационная форма успешно создана/обновлена")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @PutMapping("/save")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity save(@RequestBody OrgFormDto orgFormDto) {

        orgFormService.save(orgFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * Логически удаляет организационную форму по id.
     */
    @Operation(summary = "Удалить организационную форму",
               description = "Производит логическое удаление организационной формы")
    @ApiResponse(responseCode = "204", description = "Организационная форма удалена (is_active=false)")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.canEditReferenceData()")
    public ResponseEntity<?> delete(@PathVariable int id) {

        orgFormService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
