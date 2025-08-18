package com.webbee.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для передачи событий о контрагентах с версией.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContractorEventDto {

    private String contractorId;
    private Long version;
    private String eventType;
    private ContractorDto contractorData;
    private java.time.LocalDateTime eventTime;

}
