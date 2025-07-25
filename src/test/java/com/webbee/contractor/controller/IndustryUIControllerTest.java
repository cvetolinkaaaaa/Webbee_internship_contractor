package com.webbee.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.TestSecurityConfig;
import com.webbee.contractor.dto.IndustryDto;
import com.webbee.contractor.security.service.AuthorizationService;
import com.webbee.contractor.service.IndustryService;
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

@WebMvcTest(IndustryUIController.class)
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
class IndustryUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IndustryService industryService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ui/industry/all - успешно возвращает список индустрий для авторизованного пользователя с правом просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithViewPermission_ReturnsIndustryList() throws Exception {

        List<IndustryDto> industries = List.of(
                new IndustryDto(1, "Авиастроение", true),
                new IndustryDto(2, "IT", true)
        );
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(industryService.getAll()).thenReturn(industries);
        mockMvc.perform(get("/ui/industry/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Авиастроение"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("IT"));
        verify(industryService).getAll();
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/industry/all - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/industry/all"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(industryService);

    }

    @Test
    @DisplayName("GET /ui/industry/all - запрещает доступ неавторизованному пользователю")
    void getAll_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(get("/ui/industry/all"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(industryService, authorizationService);

    }

    @Test
    @DisplayName("GET /ui/industry/{id} - успешно возвращает индустрию по ID")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_ExistingIndustry_ReturnsIndustry() throws Exception {

        IndustryDto industry = new IndustryDto(1, "Авиастроение", true);
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(industryService.getById(1)).thenReturn(industry);
        mockMvc.perform(get("/ui/industry/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Авиастроение"))
                .andExpect(jsonPath("$.is_active").value(true));
        verify(industryService).getById(1);
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/industry/{id} - возвращает 404 для несуществующей индустрии")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_NonExistingIndustry_ReturnsNotFound() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(industryService.getById(999)).thenReturn(null);
        mockMvc.perform(get("/ui/industry/999"))
                .andExpect(status().isNotFound());
        verify(industryService).getById(999);
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/industry/{id} - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/industry/1"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(industryService);

    }

    @Test
    @DisplayName("PUT /ui/industry/save - успешно сохраняет индустрию с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"CONTRACTOR_SUPERUSER"})
    void save_WithEditPermission_ReturnsCreated() throws Exception {

        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);
        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(put("/ui/industry/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryDto)))
                .andExpect(status().isCreated());
        verify(industryService).save(any(IndustryDto.class));
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("PUT /ui/industry/save - запрещает сохранение при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void save_WithoutEditPermission_ReturnsForbidden() throws Exception {

        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);
        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(put("/ui/industry/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryDto)))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(industryService);

    }

    @Test
    @DisplayName("PUT /ui/industry/save - запрещает доступ неавторизованному пользователю")
    void save_Unauthorized_ReturnsUnauthorized() throws Exception {

        IndustryDto industryDto = new IndustryDto(1, "Авиастроение", true);
        mockMvc.perform(put("/ui/industry/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryDto)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(industryService, authorizationService);
    }

    @Test
    @DisplayName("DELETE /ui/industry/delete/{id} - успешно удаляет индустрию с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"SUPERUSER"})
    void delete_WithEditPermission_ReturnsNoContent() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(delete("/ui/industry/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(industryService).delete(1);
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("DELETE /ui/industry/delete/{id} - запрещает удаление при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_WithoutEditPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(delete("/ui/industry/delete/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(industryService);

    }

    @Test
    @DisplayName("DELETE /ui/industry/delete/{id} - запрещает доступ неавторизованному пользователю")
    void delete_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(delete("/ui/industry/delete/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(industryService, authorizationService);

    }

}
