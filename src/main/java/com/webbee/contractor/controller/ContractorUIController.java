package com.webbee.contractor.controller;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.service.ContractorService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UI контроллер для управления контрагентами с проверкой ролей.
 */
@RestController
@RequestMapping("/ui/contractor")
@Tag(name = "Contractor UI", description = "Contractor UI API с проверкой ролей")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ContractorUIController {

    private final ContractorService contractorService;

    /**
     * Получает список всех активных контрагентов.
     */
    @Operation(summary = "Получить всех контрагентов",
               description = "Возвращает список всех контрагентов")
    @ApiResponse(responseCode = "200", description = "Успешно возвращён список контрагентов")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @GetMapping("/all")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<List<ContractorDto>> getAll() {
        return ResponseEntity.ok(contractorService.getAll());
    }

    /**
     * Получает контрагента по id.
     */
    @Operation(summary = "Получить контрагента по id",
               description = "Возвращает контрагента по указанному id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Контрагент найден и возвращен"),
            @ApiResponse(responseCode = "404", description = "Контрагент не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canViewReferenceData()")
    public ResponseEntity<ContractorDto> getById(@PathVariable String id) {
        ContractorDto contractorDto = contractorService.getById(id);
        if (contractorDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(contractorDto);
    }

    /**
     * Создаёт или обновляет контрагента.
     */
    @Operation(summary = "Сохранить контрагента",
               description = "Сохраняет контрагента или изменяет существующего")
    @ApiResponse(responseCode = "201", description = "Контрагент успешно создан/обновлен")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @PutMapping("/save")
    @PreAuthorize("@authorizationService.canModifyContractors()")
    public ResponseEntity save(@RequestBody ContractorDto contractorDto) {
        contractorService.saveWithAuth(contractorDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Логически удаляет контрагента по id.
     */
    @Operation(summary = "Удалить контрагента",
               description = "Производит логическое удаление контрагента")
    @ApiResponse(responseCode = "204", description = "Контрагент удален (is_active=false)")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.canModifyContractors()")
    public ResponseEntity delete(@PathVariable String id) {
        contractorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Выполняет поиск контрагентов по фильтрам с пагинацией.
     */
    @Operation(
            summary = "Поиск контрагентов",
            description = "Возвращает контрагентов по фильтрам"
    )
    @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    @PostMapping("/search")
    @PreAuthorize("@authorizationService.canSearchContractors()")
    public ResponseEntity<List<ContractorDto>> search(@RequestBody ContractorSearchRequest contractorSearchRequest) {
        List<ContractorDto> results = contractorService.searchWithAuth(contractorSearchRequest);
        return ResponseEntity.ok(results);
    }

}
