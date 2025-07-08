package com.webbee.contractor.repository;

import com.webbee.contractor.model.OrgForm;
import org.junit.jupiter.api.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrgFormRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    OrgFormRepository orgFormRepository;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeAll
    void setUp() {
        DataSource dataSource = DataSourceBuilder.create()
                .url(postgres.getJdbcUrl())
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .driverClassName(postgres.getDriverClassName())
                .build();
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("""
            CREATE TABLE org_form (
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """);

        orgFormRepository = new OrgFormRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("save() and findById() сохраняет и получает организацию по id")
    void saveAndFindById() {
        OrgForm orgForm = new OrgForm(1, "Организация", true);
        orgFormRepository.save(orgForm);
        assertNotNull(orgForm.getId());
        OrgForm fromDb = orgFormRepository.findById(1);
        assertNotNull(fromDb);
        assertEquals("Организация", fromDb.getName());
        assertTrue(fromDb.getIsActive());
    }

    @Test
    @DisplayName("save() обновляет существующую организацию")
    void saveCountry() {
        OrgForm orgForm = new OrgForm(1, "Организация", true);
        orgFormRepository.save(orgForm);

        orgForm.setName("Новая организация");
        orgFormRepository.save(orgForm);

        OrgForm updated = orgFormRepository.findById(orgForm.getId());
        assertEquals("Новая организация", updated.getName());
    }

    @Test
    @DisplayName("delete() логически удаляет организацию")
    void delete() {
        OrgForm orgForm = new OrgForm(null, "Организация", true);
        orgFormRepository.save(orgForm);

        orgFormRepository.delete(orgForm.getId());
        OrgForm deleted = orgFormRepository.findById(orgForm.getId());
        assertNotNull(deleted);
        assertFalse(deleted.getIsActive());
    }

}