package com.webbee.contractor.controller;

import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.service.ContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * REST-контроллер для управления контрагентами.
 */
@RestController
@RequestMapping("/contractor")
public class ContractorController {

    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    /**
     * Получает список всех активных контрагентов.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ContractorDto>> getAll() {
        return ResponseEntity.ok(contractorService.getAll());
    }

    /**
     * Получает контрагента по id.
     */
    @GetMapping("/{id}")
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
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody ContractorDto contractorDto) {
        contractorService.save(contractorDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Логически удаляет контрагента по id.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        contractorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Выполняет поиск контрагентов по фильтрам с пагинацией.
     */
    @PostMapping("/search")
    public ResponseEntity<List<ContractorDto>> search(@RequestBody ContractorSearchRequest contractorSearchRequest){
        List<ContractorDto> results = contractorService.search(contractorSearchRequest);
        return ResponseEntity.ok(results);
    }

}
