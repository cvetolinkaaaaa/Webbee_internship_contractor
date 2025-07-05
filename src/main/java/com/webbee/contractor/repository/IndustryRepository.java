package com.webbee.contractor.repository;

import com.webbee.contractor.model.Industry;
import com.webbee.contractor.row_mapper.IndustryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndustryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IndustryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Industry> findAll() {
        return jdbcTemplate.query("SELECT * FROM industry WHERE is_active = TRUE", new IndustryRowMapper());
    }

    public Industry findById(int id) {
        return jdbcTemplate.query("SELECT * FROM industry WHERE id=?", new Object[]{id}, new IndustryRowMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Industry industry) {
        if (findById(industry.getId()) != null) {
            jdbcTemplate.update("UPDATE industry SET name = ?, is_active = ? WHERE id = ?",
                    industry.getName(), industry.getIsActive(), industry.getId());
        } else {
            jdbcTemplate.update("INSERT INTO industry (id, name, is_active) VALUES (?, ?, ?)",
                    industry.getId(), industry.getName(), industry.getIsActive());
        }
    }

    public void delete(int id) {
        jdbcTemplate.update("UPDATE industry SET is_active = FALSE WHERE id = ?", id);
    }

}