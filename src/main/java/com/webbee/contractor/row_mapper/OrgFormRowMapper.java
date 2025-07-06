package com.webbee.contractor.row_mapper;

import com.webbee.contractor.model.OrgForm;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrgFormRowMapper implements RowMapper<OrgForm> {

    @Override
    public OrgForm mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrgForm.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .isActive(rs.getBoolean("is_active"))
                .build();
    }

}
