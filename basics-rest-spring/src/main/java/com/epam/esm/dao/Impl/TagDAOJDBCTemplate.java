package com.epam.esm.dao.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.constant.SQLRequests;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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
        return jdbcTemplate.query(SQLRequests.GET_ALL_TAGS, new TagRowMapper());
    }

    @Override
    public Tag getTagById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        return jdbcTemplate.queryForObject(SQLRequests.GET_TAG_BY_ID, namedParameters, new TagRowMapper());
    }

    @Override
    public int addTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("tag_name", tag.getName());
        jdbcTemplate.update(SQLRequests.ADD_TAG, sqlParameterSource, keyHolder);
        return (int) keyHolder.getKeys().get("id_tag");
    }


    @Override
    public void deleteTagById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        jdbcTemplate.update(SQLRequests.DELETE_TAG_BY_ID, namedParameters);
    }

}
