package com.webbee.contractor.row_mapper;

import com.webbee.contractor.model.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {

    @Override
    public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Country.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .isActive(rs.getBoolean("is_active"))
                .build();
    }

}
