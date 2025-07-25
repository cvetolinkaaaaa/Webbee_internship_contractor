package com.webbee.contractor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Объект запроса для поиска контрагентов с фильтрами и параметрами пагинации.
 * @author Evseeva Tsvetolina
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContractorSearchRequest {

    /**
     * Id контрагента.
     */
    @Schema(description = "Id контрагента", example = "CONTRACTORID")
    private String contractorId;
    /**
     * Id родительского контрагента.
     */
    @Schema(description = "Id родительского контрагента", example = "PARENTID")
    private String parentId;
    /**
     * Строка общего поиска.
     */
    @Schema(description = "Строка общего поиска по нескольким полям", example = "Тест")
    private String contractorSearch;
    /** Фильтр по стране (необязательно). */
    @Schema(description = "Страна", example = "Россия")
    private String country;
    /** Фильтр по индустрии (необязательно). */
    @Schema(description = "Id индустрии", example = "1")
    private Integer industry;
    /** Фильтр по организации(необязательно). */
    @Schema(description = "Название организационной формы", example = "2")
    private String orgForm;
    /** Номер страницы. */
    @Schema(description = "Номер страницы", example = "1", defaultValue = "0")
    private Integer page;
    /** Размер страницы. */
    @Schema(description = "Размер страницы", example = "10", defaultValue = "10")
    private Integer size;



}
