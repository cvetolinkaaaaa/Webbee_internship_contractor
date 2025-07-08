package com.webbee.contractor.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ContractorSearchRequest {

    private String contractorId;
    private String parentId;
    private String contractorSearch;
    private String country;
    private Integer industry;
    private String orgForm;
    private Integer page;
    private Integer size;
}
