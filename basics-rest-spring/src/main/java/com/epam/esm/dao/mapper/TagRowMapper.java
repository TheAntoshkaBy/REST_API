package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int i) throws SQLException {
        return new Tag(
                rs.getInt("id_tag"),
                rs.getString("tag_name")
        );
    }
}