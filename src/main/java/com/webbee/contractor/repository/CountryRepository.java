package com.webbee.contractor.repository;

import com.webbee.contractor.row_mapper.CountryRowMapper;
import com.webbee.contractor.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий для доступа к данным о странах в базе данных через JdbcTemplate.
 */
@Repository
public class CountryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String FIND_ALL = "SELECT * FROM country WHERE is_active = TRUE";
    private static final String FIND_BY_ID = "SELECT * FROM country WHERE id = :id";
    private static final String UPDATE = "UPDATE country SET name = :name, is_active = :isActive WHERE id = :id";
    private static final String INSERT = "INSERT INTO country (id, name, is_active) VALUES (:id, :name, :isActive)";
    private static final String DELETE = "UPDATE country SET is_active = FALSE WHERE id = :id";

    @Autowired
    public CountryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Возвращает список всех активных стран.
     */
    public List<Country> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, new CountryRowMapper());
    }

    /**
     * Находит страну по id.
     */
    public Country findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.query(FIND_BY_ID, params, new CountryRowMapper())
                .stream().findAny().orElse(null);
    }

    /**
     * Создаёт новую или обновляет существующую запись о стране.
     */
    public void save(Country country) {
        if (findById(country.getId()) != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", country.getId());
            params.put("name", country.getName());
            params.put("isActive", country.getIsActive());
            namedParameterJdbcTemplate.update(UPDATE, params);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", country.getId());
            params.put("name", country.getName());
            params.put("isActive", country.getIsActive());
            namedParameterJdbcTemplate.update(INSERT, params);
        }
    }

    /**
     * Логически удаляет страну (устанавливает is_active = false).
     */
    public void delete(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(DELETE, params);
    }

}
