package com.webbee.contractor.dto;

import lombok.*;

/**
 * Объект запроса для поиска контрагентов с фильтрами и параметрами пагинации.
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
    private String contractorId;
    /**
     * Id родительского контрагента.
     */
    private String parentId;
    /**
     * Строка общего поиска.
     */
    private String contractorSearch;
    /** Фильтр по стране (необязательно). */
    private String country;
    /** Фильтр по индустрии (необязательно). */
    private Integer industry;
    /** Фильтр по организации(необязательно). */
    private String orgForm;
    /** Номер страницы. */
    private Integer page;
    /** Размер страницы. */
    private Integer size;
}
