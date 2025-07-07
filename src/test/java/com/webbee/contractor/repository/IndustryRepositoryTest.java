package com.webbee.contractor.repository;

import com.webbee.contractor.model.Industry;
import org.junit.jupiter.api.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndustryRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    IndustryRepository industryRepository;

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
            CREATE TABLE industry (
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """);

        industryRepository = new IndustryRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("save() and findById() сохраняет и получает индустрию по id")
    void saveAndFindById() {
        Industry industry = new Industry(1, "Авиастроение", true);
        industryRepository.save(industry);
        assertNotNull(industry.getId());
        Industry fromDb = industryRepository.findById(1);
        assertNotNull(fromDb);
        assertEquals("Авиастроение", fromDb.getName());
        assertTrue(fromDb.getIsActive());
    }

    @Test
    @DisplayName("save() обновляет существующую индустрию")
    void saveCountry() {
        Industry industry = new Industry(1, "Авиастроение", true);
        industryRepository.save(industry);

        industry.setName("Производство авиатехники");
        industryRepository.save(industry);

        Industry updated = industryRepository.findById(industry.getId());
        assertEquals("Производство авиатехники", updated.getName());
    }

    @Test
    @DisplayName("delete() логически удаляет индустрию")
    void delete() {
        Industry industry = new Industry(null, "Авиастроение", true);
        industryRepository.save(industry);

        industryRepository.delete(industry.getId());
        Industry deleted = industryRepository.findById(industry.getId());
        assertNotNull(deleted);
        assertFalse(deleted.getIsActive());
    }

}