package com.webbee.contractor.row_mapper;

import com.webbee.contractor.model.Contractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractorRowMapper implements RowMapper<Contractor> {

    @Override
    public Contractor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contractor contractor = new Contractor();
        contractor.setId(rs.getString("id"));
        contractor.setParentId(rs.getString("parent_id"));
        contractor.setName(rs.getString("name"));
        contractor.setNameFull(rs.getString("name_full"));
        contractor.setInn(rs.getString("inn"));
        contractor.setOgrn(rs.getString("ogrn"));
        contractor.setCountry(rs.getString("country"));
        contractor.setIndustry(rs.getObject("industry") != null ? rs.getInt("industry") : null);
        contractor.setOrgForm(rs.getObject("org_form") != null ? rs.getInt("org_form") : null);
        contractor.setCreateDate(rs.getTimestamp("create_date") != null ? rs.getTimestamp("create_date").toLocalDateTime() : null);
        contractor.setModifyDate(rs.getTimestamp("modify_date") != null ? rs.getTimestamp("modify_date").toLocalDateTime() : null);
        contractor.setCreateUserId(rs.getString("create_user_id"));
        contractor.setModifyUserId(rs.getString("modify_user_id"));
        contractor.setIsActive(rs.getBoolean("is_active"));
        contractor.setVersion(rs.getLong("version"));
        return contractor;
    }

}
