package com.webbee.contractor.repository;


import com.webbee.contractor.model.OrgForm;
import com.webbee.contractor.row_mapper.OrgFormRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrgFormRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrgFormRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrgForm> findAll() {
        return jdbcTemplate.query("SELECT * FROM org_form WHERE is_active = TRUE", new OrgFormRowMapper());
    }

    public OrgForm findById(int id) {
        return jdbcTemplate.query("SELECT * FROM org_form WHERE id=?", new Object[]{id}, new OrgFormRowMapper())
                .stream().findAny().orElse(null);
    }

    public void save(OrgForm orgForm) {
        if (findById(orgForm.getId()) != null) {
            jdbcTemplate.update("UPDATE org_form SET name = ?, is_active = ? WHERE id = ?",
                    orgForm.getName(), orgForm.getIsActive(), orgForm.getId());
        } else {
            jdbcTemplate.update("INSERT INTO org_form (id, name, is_active) VALUES (?, ?, ?)",
                    orgForm.getId(), orgForm.getName(), orgForm.getIsActive());
        }
    }
    public void delete(int id) {
        jdbcTemplate.update("UPDATE org_form SET is_active = FALSE WHERE id = ?", id);
    }

}