
package com.webbee.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbee.contractor.TestSecurityConfig;
import com.webbee.contractor.dto.CountryDto;
import com.webbee.contractor.security.service.AuthorizationService;
import com.webbee.contractor.service.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryUIController.class)
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
class CountryUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ui/country/all - успешно возвращает список стран для авторизованного пользователя с правом просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithViewPermission_ReturnsCountryList() throws Exception {

        List<CountryDto> countries = List.of(
                new CountryDto("RUS", "Россия", true),
                new CountryDto("USA", "США", true)
        );
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(countryService.getAll()).thenReturn(countries);
        mockMvc.perform(get("/ui/country/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("RUS"))
                .andExpect(jsonPath("$[0].name").value("Россия"))
                .andExpect(jsonPath("$[1].id").value("USA"))
                .andExpect(jsonPath("$[1].name").value("США"));
        verify(countryService).getAll();
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/country/all - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAll_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/country/all"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(countryService);

    }

    @Test
    @DisplayName("GET /ui/country/all - запрещает доступ неавторизованному пользователю")
    void getAll_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(get("/ui/country/all"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(countryService, authorizationService);

    }

    @Test
    @DisplayName("GET /ui/country/{id} - успешно возвращает страну по ID")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_ExistingCountry_ReturnsCountry() throws Exception {

        CountryDto country = new CountryDto("RUS", "Россия", true);
        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(countryService.getById("RUS")).thenReturn(country);
        mockMvc.perform(get("/ui/country/RUS"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("RUS"))
                .andExpect(jsonPath("$.name").value("Россия"))
                .andExpect(jsonPath("$.is_active").value(true));
        verify(countryService).getById("RUS");
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/country/{id} - возвращает 404 для несуществующей страны")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_NonExistingCountry_ReturnsNotFound() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(true);
        when(countryService.getById("XXX")).thenReturn(null);
        mockMvc.perform(get("/ui/country/XXX"))
                .andExpect(status().isNotFound());
        verify(countryService).getById("XXX");
        verify(authorizationService).canViewReferenceData();

    }

    @Test
    @DisplayName("GET /ui/country/{id} - запрещает доступ при отсутствии права просмотра")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getById_WithoutViewPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canViewReferenceData()).thenReturn(false);
        mockMvc.perform(get("/ui/country/RUS"))
                .andExpect(status().isForbidden());
        verify(authorizationService).canViewReferenceData();
        verifyNoInteractions(countryService);

    }

    @Test
    @DisplayName("PUT /ui/country/save - успешно сохраняет страну с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"CONTRACTOR_SUPERUSER"})
    void save_WithEditPermission_ReturnsCreated() throws Exception {

        CountryDto countryDto = new CountryDto("RUS", "Россия", true);
        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(put("/ui/country/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDto)))
                .andExpect(status().isCreated());
        verify(countryService).save(ArgumentMatchers.any(CountryDto.class));
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("PUT /ui/country/save - запрещает сохранение при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void save_WithoutEditPermission_ReturnsForbidden() throws Exception {

        CountryDto countryDto = new CountryDto("RUS", "Россия", true);
        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(put("/ui/country/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDto)))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(countryService);

    }

    @Test
    @DisplayName("PUT /ui/country/save - запрещает доступ неавторизованному пользователю")
    void save_Unauthorized_ReturnsUnauthorized() throws Exception {

        CountryDto countryDto = new CountryDto("RUS", "Россия", true);
        mockMvc.perform(put("/ui/country/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDto)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(countryService, authorizationService);
    }

    @Test
    @DisplayName("DELETE /ui/country/delete/{id} - успешно удаляет страну с правом редактирования")
    @WithMockUser(username = "testuser", roles = {"SUPERUSER"})
    void delete_WithEditPermission_ReturnsNoContent() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(true);
        mockMvc.perform(delete("/ui/country/delete/RUS")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(countryService).delete("RUS");
        verify(authorizationService).canEditReferenceData();

    }

    @Test
    @DisplayName("DELETE /ui/country/delete/{id} - запрещает удаление при отсутствии права редактирования")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_WithoutEditPermission_ReturnsForbidden() throws Exception {

        when(authorizationService.canEditReferenceData()).thenReturn(false);
        mockMvc.perform(delete("/ui/country/delete/RUS")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(authorizationService).canEditReferenceData();
        verifyNoInteractions(countryService);

    }

    @Test
    @DisplayName("DELETE /ui/country/delete/{id} - запрещает доступ неавторизованному пользователю")
    void delete_Unauthorized_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(delete("/ui/country/delete/RUS")
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verifyNoInteractions(countryService, authorizationService);

    }

}
