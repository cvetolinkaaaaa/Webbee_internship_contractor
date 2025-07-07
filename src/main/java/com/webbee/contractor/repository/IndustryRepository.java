package com.webbee.contractor.repository;

import com.webbee.contractor.model.Industry;
import com.webbee.contractor.row_mapper.IndustryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий для доступа к данным об индустриях в базе данных через JdbcTemplate.
 */
@Repository
public class IndustryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String FIND_ALL = "SELECT id, name, is_active FROM industry WHERE is_active = TRUE";
    private static final String FIND_BY_ID = "SELECT id, name, is_active FROM industry WHERE id = :id";
    private static final String UPDATE = "UPDATE industry SET name = :name, is_active = :isActive WHERE id = :id";
    private static final String INSERT = "INSERT INTO industry (name, is_active) VALUES (:name, :isActive) RETURNING id";
    private static final String DELETE = "UPDATE industry SET is_active = FALSE WHERE id = :id";

    @Autowired
    public IndustryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Возвращает список всех активных индустрий.
     */
    public List<Industry> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, new IndustryRowMapper());    }

    /**
     * Находит индустрию по id.
     */
    public Industry findById(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.query(FIND_BY_ID, params, new IndustryRowMapper())
                .stream().findAny().orElse(null);
    }

    /**
     * Создаёт новую или обновляет существующую запись об индустрии.
     */
    public Industry save(Industry industry) {
        if (industry.getId() == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", industry.getName());
            params.put("isActive", industry.getIsActive());
            int id = namedParameterJdbcTemplate.queryForObject(INSERT, params, Integer.class);
            industry.setId(id);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", industry.getId());
            params.put("name", industry.getName());
            params.put("isActive", industry.getIsActive());
            namedParameterJdbcTemplate.update(UPDATE, params);
        }
        return  industry;
    }

    /**
     * Логически удаляет индустрию (устанавливает is_active = false).
     */
    public void delete(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(DELETE, params);
    }

}
