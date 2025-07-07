package com.webbee.contractor.repository;

import com.webbee.contractor.model.OrgForm;
import com.webbee.contractor.row_mapper.OrgFormRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrgFormRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String FIND_ALL = "SELECT id, name, is_active FROM org_form WHERE is_active = TRUE";
    private static final String FIND_BY_ID = "SELECT id, name, is_active FROM org_form WHERE id = :id";
    private static final String UPDATE = "UPDATE org_form SET name = :name, is_active = :isActive WHERE id = :id";
    private static final String INSERT = "INSERT INTO org_form (name, is_active) VALUES (:name, :isActive) RETURNING id";
    private static final String DELETE = "UPDATE org_form SET is_active = FALSE WHERE id = :id";
    @Autowired
    public OrgFormRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<OrgForm> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, new OrgFormRowMapper());    }

    public OrgForm findById(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.query(FIND_BY_ID, params, new OrgFormRowMapper())
                .stream().findAny().orElse(null);
    }

    public OrgForm save(OrgForm orgForm) {
        if (orgForm.getId() == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", orgForm.getName());
            params.put("isActive", orgForm.getIsActive());
            Integer id = namedParameterJdbcTemplate.queryForObject(INSERT, params, Integer.class);
            orgForm.setId(id);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", orgForm.getId());
            params.put("name", orgForm.getName());
            params.put("isActive", orgForm.getIsActive());
            namedParameterJdbcTemplate.update(UPDATE, params);
        }
        return orgForm;
    }

    public void delete(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(DELETE, params);
    }

}
