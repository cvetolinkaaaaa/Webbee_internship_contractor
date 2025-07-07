package com.webbee.contractor.row_mapper;

import com.webbee.contractor.model.Industry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IndustryRowMapper implements RowMapper<Industry> {

    @Override
    public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Industry.builder()
                .id(rs.getObject("id", Integer.class))
                .name(rs.getString("name"))
                .isActive(rs.getBoolean("is_active"))
                .build();
    }

}
