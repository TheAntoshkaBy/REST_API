package com.epam.esm.dao.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagDAOJDBCTemplate implements TagDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TagDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> getAll() {
        String sql = "select * from rest_api_basics.tag";
        return jdbcTemplate.query(sql, new TagMapper());
    }

    @Override
    public Tag getTagById(int id) {
        String sql = "select * from rest_api_basics.tag where id_tag = :id";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        return jdbcTemplate.queryForObject(sql, namedParameters, new TagMapper());
    }

    @Override
    public void addTag(Tag tag) {
        String SQL = "INSERT INTO rest_api_basics.tag (tag_name, id_tag) VALUES (:tag_name, DEFAULT )";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("tag_name", tag.getName());
        jdbcTemplate.update(SQL,namedParameters);
    }


    @Override
    public void deleteTagById(int id) {
        String SQL = "DELETE FROM rest_api_basics.tag where id_tag = :id";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        jdbcTemplate.update(SQL,namedParameters);
    }

    private static final class TagMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int i) throws SQLException {
            return new Tag(
                    rs.getInt("id_tag"),
                    rs.getString("tag_name")
            );
        }
    }
}
