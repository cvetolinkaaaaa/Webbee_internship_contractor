package com.webbee.contractor.repository;

import com.webbee.contractor.model.Country;
import org.junit.jupiter.api.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    CountryRepository countryRepository;

    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp() {
        DataSource dataSource = DataSourceBuilder.create()
                .url(postgres.getJdbcUrl())
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .driverClassName(postgres.getDriverClassName())
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("""
            CREATE TABLE country (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """);

        countryRepository = new CountryRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("save() and findById() сохраняет и получает страну по id")
    void saveAndFindById() {
        Country country = new Country("RUS", "Россия", true);
        countryRepository.save(country);

        Country fromDb = countryRepository.findById("RUS");
        assertNotNull(fromDb);
        assertEquals("Россия", fromDb.getName());
        assertTrue(fromDb.getIsActive());
    }

    @Test
    @DisplayName("save() обновляет существующую страну")
    void saveCountry() {
        Country country = new Country("RUS", "Россия", true);
        countryRepository.save(country);

        country.setName("Российская Федерация");
        countryRepository.save(country);

        Country updated = countryRepository.findById("RUS");
        assertEquals("Российская Федерация", updated.getName());
    }

    @Test
    @DisplayName("delete() логически удаляет страну")
    void delete() {
        Country country = new Country("USA", "США", true);
        countryRepository.save(country);

        countryRepository.delete("USA");
        Country deleted = countryRepository.findById("USA");
        assertNotNull(deleted);
        assertFalse(deleted.getIsActive());
    }

}
