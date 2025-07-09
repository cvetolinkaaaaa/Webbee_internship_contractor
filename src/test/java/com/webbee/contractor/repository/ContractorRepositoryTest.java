package com.webbee.contractor.repository;

import com.webbee.contractor.model.Contractor;
import org.junit.jupiter.api.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContractorRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    ContractorRepository contractorRepository;

    @BeforeAll
    void setUp() {
        DataSource dataSource = DataSourceBuilder.create()
                .url(postgres.getJdbcUrl())
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .driverClassName(postgres.getDriverClassName())
                .build();

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("""
            CREATE TABLE contractor (
                id VARCHAR(12) PRIMARY KEY,
                parent_id VARCHAR(12),
                name TEXT NOT NULL,
                name_full TEXT,
                inn TEXT,
                ogrn TEXT,
                country TEXT,
                industry INT,
                org_form INT,
                create_date TIMESTAMP,
                modify_date TIMESTAMP,
                create_user_id TEXT,
                modify_user_id TEXT,
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """);

        contractorRepository = new ContractorRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("save() and findById() сохраняет и возвращает контрагента")
    void saveAndFindById() {
        Contractor contractor = new Contractor();
        contractor.setId("QWERTY");
        contractor.setName("ООО Тест");
        contractor.setIsActive(true);
        contractor.setCreateDate(LocalDateTime.now());
        contractorRepository.save(contractor);

        Contractor fromDb = contractorRepository.findById("QWERTY");
        assertNotNull(fromDb);
        assertEquals("ООО Тест", fromDb.getName());
        assertTrue(fromDb.getIsActive());
    }

    @Test
    @DisplayName("save() обновляет контрагента")
    void updateContractor() {
        Contractor contractor = new Contractor();
        contractor.setId("ASDFG");
        contractor.setName("SaveTest");
        contractor.setIsActive(true);
        contractorRepository.save(contractor);

        contractor.setName("New SaveTest");
        contractorRepository.save(contractor);

        Contractor contractorUpdated = contractorRepository.findById("ASDFG");
        assertEquals("New SaveTest", contractorUpdated.getName());
    }

    @Test
    @DisplayName("delete() логически удаляет контрагента")
    void delete() {
        Contractor contractor = new Contractor();
        contractor.setId("ZXCVB");
        contractor.setName("To Delete");
        contractor.setIsActive(true);
        contractorRepository.save(contractor);

        contractorRepository.delete("ZXCVB");
        Contractor contractorDeleted = contractorRepository.findById("ZXCVB");
        assertNotNull(contractorDeleted);
        assertFalse(contractorDeleted.getIsActive());
    }

}
