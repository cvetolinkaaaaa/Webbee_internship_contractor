package com.webbee.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.TestSecurityConfig;
import com.webbee.contractor.dto.ContractorDto;
import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.security.service.AuthorizationService;
import com.webbee.contractor.service.ContractorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractorUIController.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration," +
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "jwt.secret=TestSecret",
    "spring.liquibase.enabled=false",
    "spring.jpa.hibernate.ddl-auto=none"
})
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestSecurityConfig.class)
class ContractorUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContractorService contractorService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ui/contractor/all - успешно возвращает список контрагентов для авторизованного пользователя с правом просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithViewPermission_ReturnsContractorList() throws Exception {

        List<ContractorDto> contractors = List.of(
                ContractorDto.builder()
                        .id("CNT001")
                        .name("ООО Тест")
                        .inn("1234567890")
                        .isActive(true)
                        .build(),
                ContractorDto.builder()
                        .id("CNT002")
                        .name("ЗАО Пример")
                        .inn("0987654321")
                        .isActive(true)
                        .build()
        );
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(contractorService.getAll()).thenReturn(contractors);
        mockMvc.perform(get("/ui/contractor/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("CNT001"))
                .andExpect(jsonPath("$[0].name").value("ООО Тест"))
                .andExpect(jsonPath("$[1].id").value("CNT002"))
                .andExpect(jsonPath("$[1].name").value("ЗАО Пример"));
        verify(contractorService).getAll();
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/contractor/all - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/contractor/all"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(contractorService);

    }

    @Test
    @DisplayName("GET /ui/contractor/all - запрещает доступ неавторизованному пользователю")
    void getAll_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(get("/ui/contractor/all"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(contractorService, authorizationService);

    }

    @Test
    @DisplayName("GET /ui/contractor/{id} - успешно возвращает контрагента по ID")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_ExistingContractor_ReturnsContractor() throws Exception {

        ContractorDto contractor = ContractorDto.builder()
                .id("CNT001")
                .name("ООО Тест")
                .inn("1234567890")
                .isActive(true)
                .build();
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(contractorService.getById("CNT001")).thenReturn(contractor);
        mockMvc.perform(get("/ui/contractor/CNT001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("CNT001"))
                .andExpect(jsonPath("$.name").value("ООО Тест"))
                .andExpect(jsonPath("$.inn").value("1234567890"));
        verify(contractorService).getById("CNT001");
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/contractor/{id} - возвращает 404 для несуществующего контрагента")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_NonExistingContractor_ReturnsNotFound() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(contractorService.getById("NO")).thenReturn(null);
        mockMvc.perform(get("/ui/contractor/NO"))
                .andExpect(status().isNotFound());
        verify(contractorService).getById("NO");
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/contractor/{id} - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/contractor/CNT001"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(contractorService);

    }

    @Test
    @DisplayName("PUT /ui/contractor/save - успешно сохраняет контрагента с правом модификации")
    @WithMockUser(username = "testuser", roles = {"CONTRACTOR_SUPERUSER"})
    void save_WithModifyPermission_ReturnsCreated() throws Exception {

        ContractorDto contractorDto = ContractorDto.builder()
                .id("CNT001")
                .name("ООО Тест")
                .inn("1234567890")
                .isActive(true)
                .build();
        when(authorizationService.canModifyContractors()).thenReturn(true);
        mockMvc.perform(put("/ui/contractor/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractorDto)))
                .andExpect(status().isCreated());
        verify(contractorService).saveWithAuth(any(ContractorDto.class));
        verify(authorizationService).canModifyContractors();

    }

    @Test
    @DisplayName("PUT /ui/contractor/save - запрещает сохранение при отсутствии права модификации")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void save_WithoutModifyPermission_ReturnsForbidden() throws Exception {

        ContractorDto contractorDto = ContractorDto.builder()
                .id("CNT001")
                .name("ООО Тест")
                .inn("1234567890")
                .isActive(true)
                .build();
        when(authorizationService.canModifyContractors()).thenReturn(false);
        mockMvc.perform(put("/ui/contractor/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractorDto)))
                .andExpect(status().isForbidden());
        verify(authorizationService).canModifyContractors();
        verifyNoInteractions(contractorService);

    }

    @Test
    @DisplayName("PUT /ui/contractor/save - запрещает доступ неавторизованному пользователю")
    void save_Unauthorized_ReturnsUnauthorized() throws Exception {

        ContractorDto contractorDto = ContractorDto.builder()
                .id("CNT001")
                .name("ООО Тест")
                .inn("1234567890")
                .isActive(true)
                .build();
        mockMvc.perform(put("/ui/contractor/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractorDto)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(contractorService, authorizationService);

    }

    @Test
    @DisplayName("DELETE /ui/contractor/delete/{id} - успешно удаляет контрагента с правом модификации")
    @WithMockUser(username = "testuser", roles = {"SUPERUSER"})
    void delete_WithModifyPermission_ReturnsNoContent() throws Exception {

        when(authorizationService.canModifyContractors()).thenReturn(true);
        mockMvc.perform(delete("/ui/contractor/delete/CNT001")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(contractorService).delete("CNT001");
        verify(authorizationService).canModifyContractors();

    }

    @Test
    @DisplayName("DELETE /ui/contractor/delete/{id} - запрещает удаление при отсутствии права модификации")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_WithoutModifyPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canModifyContractors()).thenReturn(false);
        mockMvc.perform(delete("/ui/contractor/delete/CNT001")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(authorizationService).canModifyContractors();
        verifyNoInteractions(contractorService);

    }

    @Test
    @DisplayName("DELETE /ui/contractor/delete/{id} - запрещает доступ неавторизованному пользователю")
    void delete_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(delete("/ui/contractor/delete/CNT001")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(contractorService, authorizationService);

    }

    @Test
    @DisplayName("POST /ui/contractor/search - успешно выполняет поиск контрагентов с правом поиска")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void search_WithSearchPermission_ReturnsResults() throws Exception {

        ContractorSearchRequest searchRequest = ContractorSearchRequest.builder()
                .contractorSearch("тест")
                .page(0)
                .size(10)
                .build();
        List<ContractorDto> results = List.of(
                ContractorDto.builder()
                        .id("CNT001")
                        .name("ООО Тест")
                        .inn("1234567890")
                        .isActive(true)
                        .build()
        );
        when(authorizationService.canSearchContractors()).thenReturn(true);
        when(contractorService.searchWithAuth(any(ContractorSearchRequest.class))).thenReturn(results);
        mockMvc.perform(post("/ui/contractor/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("CNT001"))
                .andExpect(jsonPath("$[0].name").value("ООО Тест"));
        verify(contractorService).searchWithAuth(any(ContractorSearchRequest.class));
        verify(authorizationService).canSearchContractors();

    }

    @Test
    @DisplayName("POST /ui/contractor/search - запрещает поиск при отсутствии права поиска")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void search_WithoutSearchPermission_ReturnsForbidden() throws Exception {

        ContractorSearchRequest searchRequest = ContractorSearchRequest.builder()
                .contractorSearch("тест")
                .page(0)
                .size(10)
                .build();
        when(authorizationService.canSearchContractors()).thenReturn(false);
        mockMvc.perform(post("/ui/contractor/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isForbidden());
        verify(authorizationService).canSearchContractors();
        verifyNoInteractions(contractorService);

    }

    @Test
    @DisplayName("POST /ui/contractor/search - запрещает доступ неавторизованному пользователю")
    void search_Unauthorized_ReturnsUnauthorized() throws Exception {

        ContractorSearchRequest searchRequest = ContractorSearchRequest.builder()
                .contractorSearch("тест")
                .page(0)
                .size(10)
                .build();
        mockMvc.perform(post("/ui/contractor/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(contractorService, authorizationService);

    }

}