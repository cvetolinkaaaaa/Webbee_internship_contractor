package com.webbee.contractor.repository;

import com.webbee.contractor.row_mapper.CountryRowMapper;
import com.webbee.contractor.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для доступа к данным о странах в базе данных через JdbcTemplate.
 */
@Repository
public class CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CountryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Возвращает список всех активных стран.
     */
    public List<Country> findAll() {
        return jdbcTemplate.query("SELECT * FROM country WHERE is_active = TRUE", new CountryRowMapper());
    }

    /**
     * Находит страну по id.
     */
    public Country findById(String id) {
        return jdbcTemplate.query("SELECT * FROM country WHERE id=?", new Object[]{id}, new CountryRowMapper())
                .stream().findAny().orElse(null);
    }

    /**
     * Создаёт новую или обновляет существующую запись о стране.
     */
    public void save(Country country) {
        if (findById(country.getId()) != null) {
            jdbcTemplate.update("UPDATE country SET name = ?, is_active = ? WHERE id = ?",
                    country.getName(), country.getIsActive(), country.getId());
        } else {
            jdbcTemplate.update("INSERT INTO country (id, name, is_active) VALUES (?, ?, ?)",
                    country.getId(), country.getName(), country.getIsActive());
        }
    }

    /**
     * Логически удаляет страну (устанавливает is_active = false).
     */
    public void delete(String id) {
        jdbcTemplate.update("UPDATE country SET is_active = FALSE WHERE id = ?", id);
    }

}
