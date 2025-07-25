package com.webbee.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.TestSecurityConfig;
import com.webbee.contractor.dto.OrgFormDto;
import com.webbee.contractor.security.service.AuthorizationService;
import com.webbee.contractor.service.OrgFormService;
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

@WebMvcTest(OrgFormUIController.class)
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
class OrgFormUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrgFormService orgFormService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ui/org_form/all - успешно возвращает список организационных форм для авторизованного пользователя с правом просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithViewPermission_ReturnsOrgFormList() throws Exception {

        List<OrgFormDto> orgForms = List.of(
                new OrgFormDto(1, "ООО", true),
                new OrgFormDto(2, "ЗАО", true)
        );
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(orgFormService.getAll()).thenReturn(orgForms);
        mockMvc.perform(get("/ui/org_form/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("ООО"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("ЗАО"));
        verify(orgFormService).getAll();
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/org_form/all - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/org_form/all"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(orgFormService);

    }

    @Test
    @DisplayName("GET /ui/org_form/all - запрещает доступ неавторизованному пользователю")
    void getAll_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(get("/ui/org_form/all"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orgFormService, authorizationService);

    }

    @Test
    @DisplayName("GET /ui/org_form/{id} - успешно возвращает организационную форму по ID")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_ExistingOrgForm_ReturnsOrgForm() throws Exception {

        OrgFormDto orgForm = new OrgFormDto(1, "ООО", true);
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(orgFormService.getById(1)).thenReturn(orgForm);
        mockMvc.perform(get("/ui/org_form/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ООО"))
                .andExpect(jsonPath("$.is_active").value(true));
        verify(orgFormService).getById(1);
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/org_form/{id} - возвращает 404 для несуществующей организационной формы")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_NonExistingOrgForm_ReturnsNotFound() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(orgFormService.getById(999)).thenReturn(null);
        mockMvc.perform(get("/ui/org_form/999"))
                .andExpect(status().isNotFound());
        verify(orgFormService).getById(999);
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/org_form/{id} - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/org_form/1"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(orgFormService);

    }

    @Test
    @DisplayName("PUT /ui/org_form/save - успешно сохраняет организационную форму с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"CONTRACTOR_SUPERUSER"})
    void save_WithEditPermission_ReturnsCreated() throws Exception {

        OrgFormDto orgFormDto = new OrgFormDto(1, "ООО", true);
        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(put("/ui/org_form/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgFormDto)))
                .andExpect(status().isCreated());
        verify(orgFormService).save(any(OrgFormDto.class));
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("PUT /ui/org_form/save - запрещает сохранение при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void save_WithoutEditPermission_ReturnsForbidden() throws Exception {

        OrgFormDto orgFormDto = new OrgFormDto(1, "ООО", true);
        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(put("/ui/org_form/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgFormDto)))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(orgFormService);

    }

    @Test
    @DisplayName("PUT /ui/org_form/save - запрещает доступ неавторизованному пользователю")
    void save_Unauthorized_ReturnsUnauthorized() throws Exception {

        OrgFormDto orgFormDto = new OrgFormDto(1, "ООО", true);
        mockMvc.perform(put("/ui/org_form/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgFormDto)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orgFormService, authorizationService);

    }

    @Test
    @DisplayName("DELETE /ui/org_form/delete/{id} - успешно удаляет организационную форму с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"SUPERUSER"})
    void delete_WithEditPermission_ReturnsNoContent() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(delete("/ui/org_form/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(orgFormService).delete(1);
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("DELETE /ui/org_form/delete/{id} - запрещает удаление при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_WithoutEditPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(delete("/ui/org_form/delete/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(orgFormService);

    }

    @Test
    @DisplayName("DELETE /ui/org_form/delete/{id} - запрещает доступ неавторизованному пользователю")
    void delete_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(delete("/ui/org_form/delete/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orgFormService, authorizationService);

    }

}